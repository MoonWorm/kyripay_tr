/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
abstract class GenericNotification
{
  @NotNull(message = "User id must be specified")
  @ApiModelProperty(value = "Unique user id of the recipient", example = "123")
  private Long userId;

  @NotNull(message = "User id must be specified")
  @ApiModelProperty(value = "System sender type of the notification that keep responsibility of the sending message. " +
      "Can be used for setting a proper system sender for a message, e.g. email or sender name.")
  private Sender sender;

  @NotBlank(message = "Template id for notification content must not be blank")
  @ApiModelProperty(value = "Template identifier that will be used for message body composing. " +
      "All passed parameters can be applied as a placeholders.", example = "payment_completed_body.twig")
  private String bodyTemplateId;

  @ApiModelProperty(value = "Map of key-value pairs that can be used in title and body templates as a placeholders")
  private Map<String, Object> parameters;

}