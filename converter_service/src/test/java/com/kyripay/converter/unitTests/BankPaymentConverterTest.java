package com.kyripay.converter.unitTests;

import com.kyripay.converter.converters.BankPaymentConverter;
import com.kyripay.converter.converters.Converter;
import com.kyripay.converter.dto.Amount;
import com.kyripay.converter.dto.Currency;
import com.kyripay.converter.dto.FormatDetails;
import com.kyripay.converter.dto.Payment;
import com.kyripay.converter.dto.PaymentDetails;
import com.kyripay.converter.dto.RecipientInfo;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class BankPaymentConverterTest
{
    private Converter converter = new BankPaymentConverter();

    @Test
    public void getFormatDetails()
    {
        FormatDetails formatDetails = converter.getFormatDetails();
        assertEquals("BANK_PAYMENT", formatDetails.getId());
    }


    @Test
    public void convert() throws IOException
    {
        Payment payment = Payment.builder()
            .id("ID")
            .status(Payment.Status.COMPLETED)
            .userId("testUser")
            .createdOn(123)
            .paymentDetails(PaymentDetails.builder()
                .amount(new Amount(1L, Currency.BYN))
                .bankId(123L)
                .accountNumber("ACC1")
                .recipientInfo(RecipientInfo.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .bankName("CITI")
                    .bankAddress("USA")
                    .accountNumber("ACC2")
                    .build())
                .build()
            ).build();

        assertEquals("ID\n1BYN FROM ACC1 TO ACC2", new String(converter.convert(payment)));
    }

}