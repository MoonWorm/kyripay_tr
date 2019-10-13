package com.kyripay.payment.infrastructure.mapping.converter;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentDetails;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentWithUserIdResponse;
import org.dozer.DozerConverter;

import java.time.ZoneOffset;

public class PaymentWithUserIdResponseConverter extends DozerConverter<Payment, PaymentWithUserIdResponse> {

    public PaymentWithUserIdResponseConverter() {
        super(Payment.class, PaymentWithUserIdResponse.class);
    }

    @Override
    public PaymentWithUserIdResponse convertTo(Payment src, PaymentWithUserIdResponse dest) {
        com.kyripay.payment.domain.PaymentRecipientInfo sri = src.getRecipientInfo();
        PaymentRecipientInfo recipientInfo = new PaymentRecipientInfo(sri.getFirstName(),
                sri.getLastName(), sri.getBankUrn(), sri.getBankName(), sri.getBankAddress(), sri.getAccountNumber());
        long createdOn = src.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli();
        PaymentDetails paymentDetails = new PaymentDetails(src.getAmount(), src.getBankId(),
                src.getAccountNumber(), recipientInfo);
        return new PaymentWithUserIdResponse(src.getId(), src.getUserId(), src.getStatus(), paymentDetails, createdOn);
    }

    @Override
    public Payment convertFrom(PaymentWithUserIdResponse src, Payment dest) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
