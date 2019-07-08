package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentRequest;
import org.dozer.DozerConverter;

public class PaymentRequestConverter extends DozerConverter<PaymentRequest, Payment> {

    public PaymentRequestConverter() {
        super(PaymentRequest.class, Payment.class);
    }

    @Override
    public Payment convertTo(PaymentRequest source, Payment destination) {
        if (source == null) {
            return null;
        }
        PaymentDetails paymentRequestDetails = source.getPaymentDetails();
        Amount paymentRequestAmount = source.getPaymentDetails().getAmount();
        com.kyripay.payment.dto.RecipientInfo paymentRequestRecipientInfo = paymentRequestDetails.getRecipientInfo();
        return Payment.builder()
                .bankId(paymentRequestDetails.getBankId())
                .accountNumber(paymentRequestDetails.getAccountNumber())
                .amount(new Amount(paymentRequestAmount.getAmount(), paymentRequestAmount.getCurrency()))
                .recipientInfo(
                        com.kyripay.payment.domain.RecipientInfo.builder()
                                .firstName(paymentRequestRecipientInfo.getFirstName())
                                .lastName(paymentRequestRecipientInfo.getLastName())
                                .bankName(paymentRequestRecipientInfo.getBankName())
                                .bankAddress(paymentRequestRecipientInfo.getBankAddress())
                                .accountNumber(paymentRequestRecipientInfo.getAccountNumber())
                                .build()
                )
                .build();
    }

    @Override
    public PaymentRequest convertFrom(Payment source, PaymentRequest destination) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
