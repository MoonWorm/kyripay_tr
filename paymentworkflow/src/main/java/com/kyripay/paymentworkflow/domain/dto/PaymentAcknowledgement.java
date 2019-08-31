package com.kyripay.paymentworkflow.domain.dto;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentAcknowledgement {
    private Long paymentId;
    private Payment.Status status;
}
