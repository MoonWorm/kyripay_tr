package com.kyripay.converter.converters;

import com.kyripay.converter.dto.Payment;


public class IdentityConverter implements Converter
{
  @Override
  public byte[] convert(Payment payment)
  {
    return payment.toString().getBytes();
  }
}
