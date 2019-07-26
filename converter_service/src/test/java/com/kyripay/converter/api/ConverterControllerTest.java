package com.kyripay.converter.api;


import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.exceptions.DocumentNotFoundException;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.service.ConversionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ConverterController.class)
@ActiveProfiles("test")
public class ConverterControllerTest
{
  @Autowired
  MockMvc mockMvc;

  @MockBean
  ConversionService service;

  @Before
  public void setUp() {
    when(service.convert(any(Payment.class), eq("IDENTITY"))).thenReturn("id");
    when(service.convert(any(Payment.class), eq("BAD_FORMAT"))).thenThrow(WrongFormatException.class);
    when(service.getDocument("bad_id")).thenThrow(DocumentNotFoundException.class);
  }

  @Test
  public void convertCorrectPayment() throws Exception
  {
    this.mockMvc.perform(
        post("/api/v1/converters/IDENTITY/conversion-requests")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes()))
        .andExpect(status().isAccepted())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("documentId", is("id"))
    );
  }

  @Test
  public void accountIdIsRequired() throws Exception
  {
    this.mockMvc.perform(
        post("/api/v1/converters/IDENTITY/conversion-requests")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(getClass().getResourceAsStream("/testdata/paymentWithoutAccountId.json").readAllBytes()))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void formatShouldBeCorrect() throws Exception
  {
    this.mockMvc.perform(
        post("/api/v1/converters/BAD_FORMAT/conversion-requests")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes()))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void documentNotFoundReturnsCorrectResponseCode() throws Exception
  {
    this.mockMvc.perform(
        get("/api/v1/documents/bad_id"))
        .andExpect(status().isNotFound());
  }
}
