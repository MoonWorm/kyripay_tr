/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;


/**
 * @author M-ATA
 */
@Value
public class NotificationRegistration
{
  @ApiModelProperty(value = "Unique id that can be used to track notification message status", example = "12345")
  private final String id;
}