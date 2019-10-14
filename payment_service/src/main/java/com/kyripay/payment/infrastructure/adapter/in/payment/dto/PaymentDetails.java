/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.infrastructure.adapter.in.payment.dto;

import com.kyripay.payment.domain.vo.Amount;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author M-ATA
 */
@Data
@RequiredArgsConstructor
public class PaymentDetails {

    @Valid
    @NotNull(message = "Payment amount must be specified")
    private final Amount amount;

    @NotNull(message = "Customer bank must be specified")
    @ApiModelProperty(value = "Bank name that will be used for the payment", example = "General Bank Inc.")
    private final Long bankId;

    @NotBlank(message = "Account number must be specified")
    @ApiModelProperty(value = "Account number of the mentioned bank that will be used for the payment", example = "12345")
    private final String accountNumber;

    @Valid
    @NotNull(message = "Recipient info must be specified")
    private final PaymentRecipientInfo recipientInfo;

}