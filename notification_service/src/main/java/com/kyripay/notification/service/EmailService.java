package com.kyripay.notification.service;

import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.exception.ServiceException;

public interface EmailService {

    NotificationResponse sendEmail(EmailNotificationRequest notification) throws ServiceException;

}
