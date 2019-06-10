package com.kyripay.converter.service;

import com.kyripay.converter.dto.Document;
import com.kyripay.converter.converters.Format;
import com.kyripay.converter.dto.Payment;

import java.util.Optional;


public interface ConversionService
{
  String convert(Payment payment, Format format);

  Optional<Document> getDocument(String id);
}
