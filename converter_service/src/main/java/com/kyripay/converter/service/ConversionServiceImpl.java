package com.kyripay.converter.service;

import com.kyripay.converter.converters.Converter;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.exceptions.DocumentNotFoundException;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ConversionServiceImpl implements ConversionService, ConversionRequestListener
{
  private final DocumentRepository repostiory;
  private final Map<String, Converter> availableConverters;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public String convert(Payment payment, String format)
  {
    if (!availableConverters.containsKey(format)){
      throw new WrongFormatException(String.format("Unable to get converter for the %s format", format));
    }

    String id = UUID.randomUUID().toString();
    Document document = new Document(id, format, DocumentStatus.PROCESSING, null);
    repostiory.save(document);
    eventPublisher.publishEvent(new ConversionRequestEvent(this, id, format, payment));
    return id;
  }


  @Override
  public Document getDocument(String id)
  {
    return repostiory.findById(id).orElseThrow(() -> new DocumentNotFoundException("Document not found"));
  }


  @Override
  public Map<String, FormatDetails> getFormats()
  {
    return availableConverters.entrySet().stream().collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> e.getValue().getFormatDetails())
    );
  }


  @Override
  @Async
  @EventListener
  public void callConverter(ConversionRequestEvent request)
  {
    try {
      Converter converter = availableConverters.get(request.getFormat());
      byte[] data = converter.convert(request.getPayment());
      Document document = repostiory.findById(request.getDocumentId()).orElseThrow(() -> new DocumentNotFoundException("id"));
      document.setStatus(DocumentStatus.READY);
      document.setData(data);
      repostiory.save(document);
    } catch (RuntimeException e){
      repostiory.save(new Document(request.getDocumentId(), request.getFormat(), DocumentStatus.CONVERSION_FAILED, null));
    }
  }
}
