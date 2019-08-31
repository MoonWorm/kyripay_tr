package com.kyripay.paymentworkflow.domain.service;

import com.kyripay.paymentworkflow.domain.dto.PaymentAcknowledgement;

public interface AcknowledgementProcessing {
    void processAck(PaymentAcknowledgement paymentAck);
}
