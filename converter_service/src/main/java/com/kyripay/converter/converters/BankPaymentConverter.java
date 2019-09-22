package com.kyripay.converter.converters;

import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.dto.Transaction;
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
    StringBuilder result = new StringBuilder();
    result.append(getHeader(payment));
    for (Transaction t: payment.getTransactions()){
      result.append(formatTransaction(t));
    }
    return result.toString().getBytes();
  }


  private String getHeader(Payment payment){
    return String.format("Id: %20s Bank: %20s Account: %50s Currency: %3s",
        payment.getId(), payment.getAccount().getBankId(), payment.getAccount().getNumber(), payment.getAccount().getCurrency());
  }


  private String formatTransaction(Transaction transaction)
  {
    return String.format("%3s %20s %50s",
        transaction.getCurrency(), transaction.getAmount(), transaction.getRecipient().getAccountNumber());
  }
}
