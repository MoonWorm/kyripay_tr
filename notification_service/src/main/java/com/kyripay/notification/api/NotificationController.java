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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Notification Service", description = "Sends notification messages to users through different channels " +
    "(email, sms...)")
public class NotificationController
{

  private EmailService emailService;

  public NotificationController(EmailService emailService) {
    this.emailService = emailService;
  }

  @ApiOperation("Sends an email to a user")
  @PostMapping("/api/v1/emailnotifications")
  NotificationResponse createEmailNotification(@RequestHeader long userId,
                                               @Valid @RequestBody EmailNotificationRequest notification)
  {
    String to = "aliaksei.taliuk@gmail.com"; // TODO: resolve by userId using UserService by FeignClient
    return emailService.sendSimpleMessage(to, notification);
  }


  @ApiOperation("Sends SMS text message to a user")
  @PostMapping("/api/v1/smsnotifications")
  void createSmsNotification(@RequestHeader long userId,
                             @Valid @RequestBody SmsNotificationRequest notification)
  {
    throw new UnsupportedOperationException("SMS notification is temporary unavailable");
  }

}