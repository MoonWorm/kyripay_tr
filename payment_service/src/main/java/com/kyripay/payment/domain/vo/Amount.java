/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * @author M-ATA
 */
@Data
public class Amount {

    @ApiModelProperty(value = "Payment amount in selected currency", example = "50")
    private final long amount;

    @NotNull(message = "Currency must be specified")
    @ApiModelProperty(value = "Payment currency", example = "USD")
    private final Currency currency;

    @JsonCreator
    public Amount(@JsonProperty("amount") long amount,
                  @NotNull(message = "Currency must be specified") @JsonProperty("currency") Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
}