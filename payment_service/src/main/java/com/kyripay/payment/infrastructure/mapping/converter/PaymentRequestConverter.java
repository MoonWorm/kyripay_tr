package com.kyripay.payment.infrastructure.mapping.converter;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentDetails;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRecipientInfo;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentRequest;
import org.dozer.DozerConverter;

public class PaymentRequestConverter extends DozerConverter<PaymentRequest, Payment> {

    public PaymentRequestConverter() {
        super(PaymentRequest.class, Payment.class);
    }

    @Override
    public Payment convertTo(PaymentRequest src, Payment dest) {
        PaymentDetails pd = src.getPaymentDetails();
        Amount amount = src.getPaymentDetails().getAmount();
        PaymentRecipientInfo ri = pd.getRecipientInfo();
        return new Payment(
                amount,
                pd.getBankId(),
                pd.getAccountNumber(),
                Status.CREATED,
                new com.kyripay.payment.domain.PaymentRecipientInfo(
                        ri.getFirstName(),
                        ri.getLastName(),
                        ri.getBankUrn(),
                        ri.getBankName(),
                        ri.getBankAddress(),
                        ri.getAccountNumber()
                ),
                null
        );
    }

    @Override
    public PaymentRequest convertFrom(Payment src, PaymentRequest dest) {
        throw new UnsupportedOperationException("This kind of conversion is not supported!");
    }
}
