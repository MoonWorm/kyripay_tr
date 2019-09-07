package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.EmailNotificationRequest;

public interface Notifier {
    void send(EmailNotificationRequest notificationRequest);
}
