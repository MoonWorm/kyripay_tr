package com.kyripay.converter.service;

import com.kyripay.converter.converters.Converter;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.exceptions.DocumentNotFoundException;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.repository.DocumentRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ConverterServiceTest
{
  @Mock
  private DocumentRepository documentRepository;
  @Mock
  ApplicationEventPublisher eventPublisher;
  @Mock
  Converter testConverter;


  private ConversionService conversionService;
  private ObjectMapper objectMapper = new ObjectMapper();
  private FormatDetails testFormatDetails = FormatDetails.of("test", "test", "test converter");

  @Before
  public void init() {
    when(testConverter.getFormatDetails()).thenReturn(FormatDetails.of("test", "test", "test converter"));
    conversionService = new ConversionServiceImpl(documentRepository, Map.of("IDENTITY", testConverter), eventPublisher);
  }

  @Test
  public void convertCorrectPayment() throws IOException
  {
    Payment payment = objectMapper.readValue(getClass().getResource("/okPayment.json"), Payment.class);

    conversionService.convert(payment, "IDENTITY");

    verify(documentRepository).save(argThat((Document d) ->  d.getFormat().equals("IDENTITY")));
    verify(eventPublisher).publishEvent(any(ConversionRequestEvent.class));
  }

  @Test(expected = WrongFormatException.class)
  public void wrongFormatThrowsException() throws IOException
  {
    Payment payment = objectMapper.readValue(getClass().getResource("/okPayment.json"), Payment.class);

    conversionService.convert(payment, "BAD_FORMAT");
  }

  @Test
  public void returnsCorrectFormats(){
    assertEquals(
        Map.of("IDENTITY", testFormatDetails),
        conversionService.getFormats());
  }

  @Test
  public void getExistingDocument(){
    when(documentRepository.findById("1")).thenReturn(Optional.of(mock(Document.class)));
    Document document = conversionService.getDocument("1");
    assertNotNull(document);
  }

  @Test(expected = DocumentNotFoundException.class)
  public void getNotExistingDocumentThrowsException(){
    when(documentRepository.findById("1")).thenReturn(Optional.empty());
    conversionService.getDocument("1");
  }
}
