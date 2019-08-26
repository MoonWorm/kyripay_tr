package com.kyripay.paymentworkflow.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface ConverterStreams {
    String CONVERTER_PROCESS = "converter-process";
    String CONVERTER_NOTIFICATIONS = "converter-notifications";

    @Output(CONVERTER_PROCESS)
    MessageChannel convertPayment();

    @Output(CONVERTER_NOTIFICATIONS)
    MessageChannel convertNotifications();
}
