package com.kyripay.notification.service;

import com.kyripay.notification.dao.entity.EmailNotificationDocument;
import com.kyripay.notification.dao.repository.EmailNotificationRepository;
import com.kyripay.notification.domain.vo.Status;
import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    private JavaMailSender emailSender;
    private TemplateResolver templateResolver;

    private EmailNotificationRepository repository;
    private EmailNotificationValidator validator;

    public EmailServiceImpl(JavaMailSender emailSender,
                            TemplateResolver templateResolver,
                            EmailNotificationRepository repository,
                            EmailNotificationValidator validator) {
        this.emailSender = emailSender;
        this.templateResolver = templateResolver;
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public NotificationResponse sendEmail(EmailNotificationRequest notification) {
        try {
            String subject = templateResolver.resolveText(notification.getTitleTemplateId(),
                    notification.getParameters());
            String body = templateResolver.resolveText(notification.getBodyTemplateId(), notification.getParameters());
            Status status = doSend(notification.getTo(), subject, body);

            EmailNotificationDocument notificationDocument = new EmailNotificationDocument(UUID.randomUUID(),
                    notification.getTo(), subject, body, status);
            validator.validateEmailNotification(notificationDocument);
            notificationDocument = repository.save(notificationDocument);
            return new NotificationResponse(notificationDocument.getUuid(), status);
        } catch (Exception e) {
            throw new ServiceException("Can't send an email notification", e);
        }
    }

    private Status doSend(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSentDate(new Date());
            message.setSubject(subject);
            message.setText(body);
            emailSender.send(message);
            return Status.SENT;
        } catch (MailException e) {
            LOG.error("Can't send email to {} with error: ", to, e);
            return Status.FAILED;
        }
    }

}
