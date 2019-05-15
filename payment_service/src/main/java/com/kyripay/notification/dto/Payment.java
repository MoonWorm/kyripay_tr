/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author M-ATA
 */
@Data
@AllArgsConstructor
public class Payment
{

  private PaymentDetails paymentDetails;

  @ApiModelProperty(value = "Payment status", example = "COMPLETED")
  private Status status;

}