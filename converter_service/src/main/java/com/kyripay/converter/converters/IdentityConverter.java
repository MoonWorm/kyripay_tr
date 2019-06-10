package com.kyripay.converter.converters;

import com.kyripay.converter.dto.Payment;

import java.util.concurrent.TimeUnit;


public class IdentityConverter implements Converter
{
  @Override
  public byte[] convert(Payment payment)
  {
    try {
      Thread.sleep(TimeUnit.SECONDS.toMillis(30));
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    return payment.toString().getBytes();
  }
}
