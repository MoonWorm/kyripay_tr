/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.EmailNotificationRequest;
import com.kyripay.notification.dto.NotificationResponse;
import com.kyripay.notification.dto.SmsNotificationRequest;
import com.kyripay.notification.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Notification Service", description = "Sends notification messages to users through different channels " +
        "(email, sms...)")
public class NotificationController {

    private EmailService emailService;
    private UserMicroService userMicroService;

    public NotificationController(EmailService emailService, UserMicroService userMicroService) {
        this.emailService = emailService;
        this.userMicroService = userMicroService;
    }

    @ApiOperation("Sends an email to a user")
    @PostMapping("/api/v1/emailnotifications")
    NotificationResponse createEmailNotification(@RequestHeader long userId,
                                                 @Valid @RequestBody EmailNotificationRequest notification) {
        String to = userMicroService.getUserContactDetails(userId).getEmail();
        return emailService.sendSimpleMessage(to, notification);
    }


    @ApiOperation("Sends SMS text message to a user")
    @PostMapping("/api/v1/smsnotifications")
    void createSmsNotification(@RequestHeader long userId,
                               @Valid @RequestBody SmsNotificationRequest notification) {
        throw new UnsupportedOperationException("SMS notification is not supported yet");
    }

    @FeignClient("userservice")
    interface UserMicroService {

        @RequestMapping(value = "/api/v1/users/{userId}/details", method = RequestMethod.GET)
        UserContactDetailsResponse getUserContactDetails(@PathVariable long userId);

    }

    @Data
    @NoArgsConstructor
    static class UserContactDetailsResponse {
        private String email;
    }

}