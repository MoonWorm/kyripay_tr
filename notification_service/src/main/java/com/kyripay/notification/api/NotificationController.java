/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.dto.SmsNotificationRequest;
import com.kyripay.notification.service.EmailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Notification Service", description = "Sends notification messages to users through different channels " +
        "(email, sms...)")
public class NotificationController {

    private EmailServiceImpl emailService;

    public NotificationController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @ApiOperation("Sends an email to a user")
    @PostMapping("/api/v1/emailnotifications")
    NotificationResponse createEmailNotification(@Valid @RequestBody EmailNotificationRequest notification) {
        return emailService.sendEmail(notification);
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Can't send an email")
    public void handleMailException() {

    }


    @ApiOperation("Sends SMS text message to a user")
    @PostMapping("/api/v1/smsnotifications")
    void createSmsNotification(@Valid @RequestBody SmsNotificationRequest notification) {
        throw new UnsupportedOperationException("SMS notification is not supported yet");
    }

}