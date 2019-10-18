package com.kyripay.notification.service;

import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;

public interface EmailService {

    NotificationResponse sendEmail(EmailNotificationRequest notification);

}
