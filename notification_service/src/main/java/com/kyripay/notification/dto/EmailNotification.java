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


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
public class EmailNotification extends GenericNotification
{

  @NotBlank(message = "Template id for an email title must not be blank")
  @ApiModelProperty(value = "Template identifier that will be used for title composing. " +
      "All passed parameters can be applied as a placeholders.", example = "payment_completed_title.twig")
  private String titleTemplateId;

}