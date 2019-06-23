package com.kyripay.converter.service;

import com.kyripay.converter.converters.Converter;
import com.kyripay.converter.domain.PaymentDocument;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.exceptions.DocumentNotFoundException;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.repository.DocumentRepostiory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ConversionServiceImpl implements ConversionService
{
  private final DocumentRepostiory repostiory;
  private final Map<String, Converter> availableConverters;

  @Override
  public String convert(Payment payment, String format)
  {
    String id = UUID.randomUUID().toString();
    PaymentDocument document = new PaymentDocument(id, format, DocumentStatus.PROCESSING, null);
    repostiory.save(document);

    try {
      callConverter(availableConverters.get(format), payment, document);
    } catch (NullPointerException e){
      throw new WrongFormatException(String.format("Unable to get converter for the %s format", format), e);
    }

    return id;
  }


  @Override
  public Document getDocument(String id)
  {
    PaymentDocument document = repostiory.findById(id).orElseThrow(() -> new DocumentNotFoundException("Document not found"));
    return new Document(document.getFormat(), document.getStatus(), document.getData());
  }


  @Override
  public Map<String, FormatDetails> getFormats()
  {
    return availableConverters.entrySet().stream().collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> e.getValue().getFormatDetails())
    );
  }


  private void callConverter(Converter converter, Payment payment, PaymentDocument document)
  {
      CompletableFuture.supplyAsync(() -> converter.convert(payment)).handleAsync((result, exception) -> {
            if (exception == null) {
              document.setStatus(DocumentStatus.READY);
              document.setData(result);
            }
            else {
              document.setStatus(DocumentStatus.CONVERSION_FAILED);
            }
            repostiory.save(document);
            return result;
          }
      );
    }
}
