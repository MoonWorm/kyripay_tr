package com.kyripay.paymentworkflow.api.dto;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentStatusUpdateRequest {
    Payment.Status status;
}
