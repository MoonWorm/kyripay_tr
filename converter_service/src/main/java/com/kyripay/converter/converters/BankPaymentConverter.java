package com.kyripay.converter.converters;

import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import org.springframework.stereotype.Component;


@Component(BankPaymentConverter.ID)
public class BankPaymentConverter implements Converter
{

  static final String ID = "BANK_PAYMENT";

  @Override
  public FormatDetails getFormatDetails()
  {
    return FormatDetails.of(ID, "Bank payment", "Example of bank payment format");
  }

  @Override
  public byte[] convert(Payment payment)
  {
    String result = payment.getId() + '\n' + String.format("%s%s FROM %s TO %s",
        payment.getPaymentDetails().getAmount().getAmount(),
        payment.getPaymentDetails().getAmount().getCurrency(),
        payment.getPaymentDetails().getAccountNumber(),
        payment.getPaymentDetails().getRecipientInfo().getAccountNumber()
    );
    return result.getBytes();
  }
}
