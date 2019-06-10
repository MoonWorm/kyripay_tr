package com.kyripay.converter.converters;

import com.kyripay.converter.dto.Payment;


public interface Converter
{
  byte[] convert(Payment payment);
}
