package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.dto.PaymentDetails;
import com.kyripay.payment.dto.PaymentRecipientInfo;
import com.kyripay.payment.dto.PaymentResponse;
import org.dozer.DozerConverter;

import java.time.ZoneOffset;

public class PaymentResponseConverter extends DozerConverter<Payment, PaymentResponse> {

    public PaymentResponseConverter() {
        super(Payment.class, PaymentResponse.class);
    }

    @Override
    public PaymentResponse convertTo(Payment src, PaymentResponse dest) {
        com.kyripay.payment.domain.PaymentRecipientInfo sri = src.getRecipientInfo();
        PaymentRecipientInfo recipientInfo = new PaymentRecipientInfo(sri.getFirstName(),
                sri.getLastName(), sri.getBankName(), sri.getBankAddress(), sri.getAccountNumber());
        long createdOn = src.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli();
        PaymentDetails paymentDetails = new PaymentDetails(src.getAmount(), src.getBankId(),
                src.getAccountNumber(), recipientInfo);
        return new PaymentResponse(src.getId(), src.getStatus(), paymentDetails, createdOn);
    }

    @Override
    public Payment convertFrom(PaymentResponse src, Payment dest) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
