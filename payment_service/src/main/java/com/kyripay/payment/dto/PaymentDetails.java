/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails
{

  @NotNull(message = "User id must be specified")
  @ApiModelProperty(value = "Related user id", example = "5")
  private Long userId;

  @NotBlank(message = "Template name must be specified")
  @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
  private String name;

  @Valid
  @NotNull(message = "Payment amount must be specified")
  private Amount amount;

  @NotNull(message = "Customer bank must be specified")
  @ApiModelProperty(value = "Bank name that will be used for the payment", example = "General Bank Inc.")
  private Long bankId;

  @NotBlank(message = "Account number must be specified")
  @ApiModelProperty(value = "Account number of the mentioned bank that will be used for the payment", example = "12345")
  private String accountNumber;

  @NotBlank(message = "Payment format must be specified")
  @ApiModelProperty(value = "Which payment format should be used for the payment", example = "ISO_123")
  private String paymentFormat;

  @Valid
  @NotNull(message = "Recipient info must be specified")
  private RecipientInfo recipientInfo;

}