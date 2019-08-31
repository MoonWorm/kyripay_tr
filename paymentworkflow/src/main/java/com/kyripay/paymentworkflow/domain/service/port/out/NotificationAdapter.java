package com.kyripay.paymentworkflow.domain.service.port.out;

import com.kyripay.paymentworkflow.domain.dto.EmailNotificationRequest;

public interface NotificationAdapter {
    void sendNotification(EmailNotificationRequest notificationRequest);
}
