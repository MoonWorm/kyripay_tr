package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.PaymentToSendRequest;

public interface Connector {
    void send(PaymentToSendRequest paymentToSendRequest);
}
