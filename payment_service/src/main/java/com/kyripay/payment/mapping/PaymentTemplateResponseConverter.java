package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import com.kyripay.payment.dto.RecipientInfo;
import org.dozer.DozerConverter;

import java.time.ZoneOffset;

public class PaymentTemplateResponseConverter extends DozerConverter<PaymentTemplate, PaymentTemplateResponse> {

    public PaymentTemplateResponseConverter() {
        super(PaymentTemplate.class, PaymentTemplateResponse.class);
    }

    @Override
    public PaymentTemplateResponse convertTo(PaymentTemplate source, PaymentTemplateResponse destination) {
        com.kyripay.payment.domain.RecipientInfo recipientInfo = source.getRecipientInfo();
        return PaymentTemplateResponse.builder()
                .id(source.getId())
                .name(source.getName())
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
    public PaymentTemplate convertFrom(PaymentTemplateResponse source, PaymentTemplate destination) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
