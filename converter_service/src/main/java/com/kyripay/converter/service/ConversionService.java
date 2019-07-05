package com.kyripay.converter.service;

import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;

import java.util.Map;


public interface ConversionService
{
  String convert(Payment payment, String format);

  Document getDocument(String id);

  Map<String, FormatDetails> getFormats();
}
