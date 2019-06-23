package com.kyripay.converter.domain;

import com.kyripay.converter.dto.DocumentStatus;
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
  private final String format;
  @NotNull
  private DocumentStatus status;
  private byte[] data;
}

