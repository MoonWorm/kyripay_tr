package com.kyripay.paymentworkflow.api;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


public interface AcknowledgmentStreams {
    String ACKNOWLEDGMENT_NOTIFICATIONS = "acknowledgment-notification";

    @Output(ACKNOWLEDGMENT_NOTIFICATIONS)
    MessageChannel acknowledgmentNotifications();
}
