package com.kyripay.payment.mapping;

import com.kyripay.payment.domain.PaymentTemplate;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.dto.PaymentTemplateDetails;
import com.kyripay.payment.dto.PaymentTemplateRecipientInfo;
import com.kyripay.payment.dto.PaymentTemplateRequest;
import org.dozer.DozerConverter;

public class PaymentTemplateRequestConverter extends DozerConverter<PaymentTemplateRequest, PaymentTemplate> {

    public PaymentTemplateRequestConverter() {
        super(PaymentTemplateRequest.class, PaymentTemplate.class);
    }

    @Override
    public PaymentTemplate convertTo(PaymentTemplateRequest src, PaymentTemplate dest) {
        PaymentTemplateDetails pd = src.getPaymentDetails();
        Amount amount = src.getPaymentDetails().getAmount();
        PaymentTemplateRecipientInfo ri = pd.getRecipientInfo();
        return new PaymentTemplate(null,
                src.getName(),
                amount,
                pd.getBankId(),
                pd.getAccountNumber(),
                new com.kyripay.payment.domain.PaymentTemplateRecipientInfo(
                        ri.getFirstName(),
                        ri.getLastName(),
                        ri.getBankName(),
                        ri.getBankAddress(),
                        ri.getAccountNumber()
                ),
                null
        );
    }

    @Override
    public PaymentTemplateRequest convertFrom(PaymentTemplate src, PaymentTemplateRequest dest) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
