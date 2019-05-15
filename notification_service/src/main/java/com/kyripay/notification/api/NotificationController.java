/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import com.kyripay.notification.dto.Notification;
import com.kyripay.notification.dto.NotificationRegistration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author M-ATA
 */
@RestController
@Api(value = "Notification Service", description = "Sends notification messages to users through different channels " +
    "(email, sms...)")
public class NotificationController
{

  @ApiOperation("Sends notification message of the selected type to a user")
  @PostMapping("/api/v1/notifications")
  NotificationRegistration create(@RequestBody Notification notification)
  {
    return new NotificationRegistration(notification.getUserId() + "abc123");
  }

}