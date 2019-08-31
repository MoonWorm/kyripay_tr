package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.domain.dto.PaymentAcknowledgement;
import com.kyripay.paymentworkflow.domain.service.AcknowledgementProcessing;
import com.kyripay.paymentworkflow.stream.AcknowledgementStreams;
import com.kyripay.paymentworkflow.stream.ConnectorStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;

public class AcknowledgementApi {

    @Autowired
    private AcknowledgementProcessing acknowledgementProcessing;

    @StreamListener(ConnectorStreams.CONNECTOR_NOTIFICATIONS)
    void processPaymentCommunicationAcknowledgement(PaymentAcknowledgement paymentAcknowledgement) {
        acknowledgementProcessing.processAck(paymentAcknowledgement);
    }

    @StreamListener(AcknowledgementStreams.ACKNOWLEDGEMENT_NOTIFICATIONS)
    void processPaymentBankAcknowledgement(PaymentAcknowledgement paymentAcknowledgement) {
        acknowledgementProcessing.processAck(paymentAcknowledgement);
    }
}
