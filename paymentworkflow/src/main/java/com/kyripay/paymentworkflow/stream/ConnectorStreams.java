package com.kyripay.paymentworkflow.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ConnectorStreams {
    String CONNECTOR_SEND = "connector-send";
    String CONNECTOR_NOTIFICATIONS = "connector-notifications";

    @Output(CONNECTOR_SEND)
    MessageChannel sendPayment();

    @Input(CONNECTOR_NOTIFICATIONS)
    MessageChannel connectorNotification();
}
