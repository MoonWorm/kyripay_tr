package com.kyripay.converter.service;

import com.kyripay.converter.converters.Converter;
import com.kyripay.converter.converters.Format;
import com.kyripay.converter.domain.PaymentDocument;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.exceptions.WrongFormatException;
import com.kyripay.converter.repository.DocumentRepostiory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Service
public class ConversionServiceImpl implements ConversionService
{

  private final DocumentRepostiory repostiory;


  public ConversionServiceImpl(DocumentRepostiory repostiory)
  {
    this.repostiory = repostiory;
  }


  @Override
  public String convert(Payment payment, Format format)
  {
    String id = UUID.randomUUID().toString();
    PaymentDocument document = new PaymentDocument(id, format, DocumentStatus.PROCESSING, null);
    repostiory.save(document);

    try {
      callConverter(format.getConverterClass().getDeclaredConstructor().newInstance(), payment, document);
    } catch (ReflectiveOperationException e){
      throw new WrongFormatException(String.format("Unable to get converter for the %s format", format.getFormatName()), e);
    }

    return id;
  }


  @Override
  public Optional<Document> getDocument(String id)
  {
    PaymentDocument document = repostiory.findById(id).orElse(null);
    if (document != null)
      return Optional.of(new Document(document.getFormat(), document.getStatus(), document.getData()));
    else
      return Optional.empty();
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
