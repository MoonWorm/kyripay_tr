/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
public class Notification
{
  @ApiModelProperty(value = "Unique user id of the recipient", example = "123")
  private String userId;
  @ApiModelProperty(value = "System sender type of the notification that keep responsibility of the sending message. " +
      "Can be used for setting a proper system sender for a message, e.g. email or sender name.")
  private Sender sender;
  @ApiModelProperty(value = "Template identifier that will be used for title composing. " +
      "All passed parameters can be applied as a placeholders.", example = "payment_completed_title.twig")
  private String titleTemplateId;
  @ApiModelProperty(value = "Template identifier that will be used for message body composing. " +
      "All passed parameters can be applied as a placeholders.", example = "payment_completed_body.twig")
  private String bodyTemplateId;
  @ApiModelProperty(value = "Map of key-value pairs that can be used in title and body templates as a placeholders")
  private Map<String, Object> parameters;
  @ApiModelProperty(value = "Transportation channel type, e.g. EMAIL, SMS ...")
  private Type type;

  enum Sender {
    CUSTOMER_SERVICE, TECHNICAL_SUPPORT
  }

  enum  Type
  {
    EMAIL, SMS
  }
}