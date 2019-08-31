package com.kyripay.paymentworkflow.domain.service.impl;

import com.kyripay.paymentworkflow.domain.dto.PaymentAcknowledgement;
import com.kyripay.paymentworkflow.domain.service.AcknowledgementProcessing;
import org.springframework.stereotype.Service;

@Service
public class AcknowledgementProcessingImpl implements AcknowledgementProcessing {
    @Override
    public void processAck(PaymentAcknowledgement paymentAck) {

    }
}
