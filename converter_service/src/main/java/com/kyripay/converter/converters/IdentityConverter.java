package com.kyripay.converter.converters;

import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import org.springframework.stereotype.Component;


@Component(IdentityConverter.ID)
public class IdentityConverter implements Converter
{
  static final String ID = "IDENTITY";

  @Override
  public FormatDetails getFormatDetails()
  {
    return FormatDetails.of(
        ID, "Identity", "String representation of initial payment"
    );
  }


  @Override
  public byte[] convert(Payment payment)
  {
    return payment.toString().getBytes();
  }
}
