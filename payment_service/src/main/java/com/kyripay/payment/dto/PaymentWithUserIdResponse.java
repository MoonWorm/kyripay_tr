/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import com.kyripay.payment.domain.vo.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;


/**
 * @author M-ATA
 */
@Data
@RequiredArgsConstructor
public class PaymentWithUserIdResponse {

    @ApiModelProperty(value = "Unique identifier", example = "4")
    private final long id;

    @ApiModelProperty(value = "User unique identifier which payment is belong to",
            example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private final UUID userId;

    @ApiModelProperty(value = "Payment status", example = "COMPLETED")
    private final Status status;

    private final PaymentDetails paymentDetails;

    @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
    private final long createdOn;

}