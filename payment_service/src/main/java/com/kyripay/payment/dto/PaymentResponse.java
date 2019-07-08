/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import com.kyripay.payment.domain.vo.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


/**
 * @author M-ATA
 */
@Data
@AllArgsConstructor
@Builder
public class PaymentResponse
{

  @ApiModelProperty(value = "Unique identifier", example = "4")
  private long id;

  @ApiModelProperty(value = "Payment status", example = "COMPLETED")
  private Status status;

  private PaymentDetails paymentDetails;

  @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
  private long createdOn;

}