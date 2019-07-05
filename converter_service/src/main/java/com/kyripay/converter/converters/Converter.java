package com.kyripay.converter.converters;

import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;


public interface Converter
{
  FormatDetails getFormatDetails();

  byte[] convert(Payment payment);
}
