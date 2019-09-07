package com.kyripay.paymentworkflow.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface NotifierStreams {
    String NOTIFICATION_SEND = "notifications-send";

    @Output(NOTIFICATION_SEND)
    MessageChannel sendNotification();
}
