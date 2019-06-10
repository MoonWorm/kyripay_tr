package com.kyripay.converter.domain;

import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.converters.Format;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


@Document
@Data
@AllArgsConstructor
public class PaymentDocument
{
  @Id
  private final String id;
  @NotNull
  private final Format format;
  @NotNull
  private DocumentStatus status;
  private byte[] data;
}

