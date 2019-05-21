/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author M-ATA
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount
{
  @NotNull(message = "Amount must be specified")
  @ApiModelProperty(value = "Payment amount in selected currency", example = "50")
  private Long amount;

  @NotNull(message = "Currency must be specified")
  @ApiModelProperty(value = "Payment currency", example = "USD")
  private Currency currency;

}