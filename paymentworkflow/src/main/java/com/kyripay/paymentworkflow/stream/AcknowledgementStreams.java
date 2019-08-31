package com.kyripay.paymentworkflow.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;


public interface AcknowledgementStreams {
    String ACKNOWLEDGEMENT_NOTIFICATIONS = "acknowledgement-notification";

    @Input(ACKNOWLEDGEMENT_NOTIFICATIONS)
    MessageChannel acknowledgementNotifications();
}
