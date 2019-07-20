package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import org.dozer.DozerConverter;

public class PaymentTemplateRequestConverter extends DozerConverter<PaymentTemplateRequest, PaymentTemplate> {

    public PaymentTemplateRequestConverter() {
        super(PaymentTemplateRequest.class, PaymentTemplate.class);
    }

    @Override
    public PaymentTemplate convertTo(PaymentTemplateRequest source, PaymentTemplate destination) {
        PaymentDetails paymentRequestDetails = source.getPaymentDetails();
        Amount paymentRequestAmount = source.getPaymentDetails().getAmount();
        com.kyripay.payment.dto.RecipientInfo paymentRequestRecipientInfo = paymentRequestDetails.getRecipientInfo();
        return PaymentTemplate.builder()
                .name(source.getName())
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
    public PaymentTemplateRequest convertFrom(PaymentTemplate source, PaymentTemplateRequest destination) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
