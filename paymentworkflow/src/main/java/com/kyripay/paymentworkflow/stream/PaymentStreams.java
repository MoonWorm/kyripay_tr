package com.kyripay.paymentworkflow.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;


public interface PaymentStreams {
    String PAYMENT_PROCESS = "payment-workflow-process";

    @Input(PAYMENT_PROCESS)
    SubscribableChannel process();
}
