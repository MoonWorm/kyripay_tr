package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentResponse;
import com.kyripay.payment.dto.RecipientInfo;
import org.dozer.DozerConverter;

import java.time.ZoneId;
import java.time.ZoneOffset;

public class PaymentResponseConverter extends DozerConverter<Payment, PaymentResponse> {

    public PaymentResponseConverter() {
        super(Payment.class, PaymentResponse.class);
    }

    @Override
    public PaymentResponse convertTo(Payment source, PaymentResponse destination) {
        com.kyripay.payment.domain.RecipientInfo recipientInfo = source.getRecipientInfo();
        return PaymentResponse.builder()
                .id(source.getId())
                .status(source.getStatus())
                .createdOn(source.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli())
                .paymentDetails(
                        PaymentDetails.builder()
                                .amount(source.getAmount())
                                .bankId(source.getBankId())
                                .accountNumber(source.getAccountNumber())
                                .recipientInfo(
                                        RecipientInfo.builder()
                                                .firstName(recipientInfo.getFirstName())
                                                .lastName(recipientInfo.getLastName())
                                                .bankName(recipientInfo.getBankName())
                                                .bankAddress(recipientInfo.getBankAddress())
                                                .accountNumber(recipientInfo.getAccountNumber())
                                                .build()
                                )
                                .build())
                .build();
    }

    @Override
    public Payment convertFrom(PaymentResponse source, Payment destination) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
