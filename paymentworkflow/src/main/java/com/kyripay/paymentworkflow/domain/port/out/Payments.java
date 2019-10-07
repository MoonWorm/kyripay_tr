package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.api.dto.PaymentStatusUpdateRequest;
import com.kyripay.paymentworkflow.domain.dto.payment.Payment;

import java.util.Optional;

public interface Payments {
    Optional<Payment> getPaymentById(long id);
    void updateStatus(long id, PaymentStatusUpdateRequest updateRequest);
}
