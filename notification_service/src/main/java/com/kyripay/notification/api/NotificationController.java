/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.EmailNotification;
import com.kyripay.notification.dto.SmsNotification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @ApiOperation("Sends an email to a user")
  @PostMapping("/api/v1/emailnotifications")
  void createEmailNotification(@Valid @RequestBody EmailNotification notification)
  {

  }


  @ApiOperation("Sends SMS text message to a user")
  @PostMapping("/api/v1/smsnotifications")
  void createSmsNotification(@Valid @RequestBody SmsNotification notification)
  {

  }

}