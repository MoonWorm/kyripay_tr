package com.kyripay.converter.service;

import com.kyripay.converter.converters.Format;
import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.Payment;


public interface ConversionService
{
  String convert(Payment payment, Format format);

  Document getDocument(String id);
}
