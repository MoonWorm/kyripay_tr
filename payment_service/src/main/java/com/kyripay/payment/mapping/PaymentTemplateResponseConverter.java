package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.dto.PaymentTemplateDetails;
import com.kyripay.payment.dto.PaymentTemplateRecipientInfo;
import com.kyripay.payment.dto.PaymentTemplateResponse;
import org.dozer.DozerConverter;

import java.time.ZoneOffset;

public class PaymentTemplateResponseConverter extends DozerConverter<PaymentTemplate, PaymentTemplateResponse> {

    public PaymentTemplateResponseConverter() {
        super(PaymentTemplate.class, PaymentTemplateResponse.class);
    }

    @Override
    public PaymentTemplateResponse convertTo(PaymentTemplate src, PaymentTemplateResponse dest) {
        com.kyripay.payment.domain.PaymentTemplateRecipientInfo sri = src.getRecipientInfo();
        PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo(sri.getFirstName(),
                sri.getLastName(), sri.getBankUrn(), sri.getBankName(), sri.getBankAddress(), sri.getAccountNumber());
        long createdOn = src.getCreatedOn().toInstant(ZoneOffset.UTC).toEpochMilli();
        PaymentTemplateDetails paymentDetails = new PaymentTemplateDetails(src.getAmount(), src.getBankId(),
                src.getAccountNumber(), recipientInfo);
        return new PaymentTemplateResponse(src.getId(), src.getName(), paymentDetails, createdOn);
    }

    @Override
    public PaymentTemplate convertFrom(PaymentTemplateResponse src, PaymentTemplate dest) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
