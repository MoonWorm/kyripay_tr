/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author M-ATA
 */
@Data
@RequiredArgsConstructor
public class Amount {

    @ApiModelProperty(value = "Payment amount in selected currency", example = "50")
    private final long amount;

    @NotNull(message = "Currency must be specified")
    @ApiModelProperty(value = "Payment currency", example = "USD")
    private final Currency currency;

}