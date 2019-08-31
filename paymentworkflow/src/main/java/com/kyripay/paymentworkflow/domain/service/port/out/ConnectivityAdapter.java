package com.kyripay.paymentworkflow.domain.service.port.out;

import com.kyripay.paymentworkflow.domain.dto.PaymentToSendRequest;

public interface ConnectivityAdapter {
    public void sendPayment(PaymentToSendRequest paymentToSendRequest);
}
