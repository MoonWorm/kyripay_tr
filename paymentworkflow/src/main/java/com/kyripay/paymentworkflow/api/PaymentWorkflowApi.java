package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.dto.Payment;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@EnableBinding({AcknowledgmentStreams.class, ConverterStreams.class, PaymentStreams.class})
class PaymentWorkflowApi {

    @StreamListener(PaymentStreams.PAYMENT_PROCESS)
    @SendTo(ConverterStreams.CONVERTER_PROCESS)
    private Payment processPayment(Payment payment) {
        return payment;
    }
}