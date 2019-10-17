package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    @NotNull(message = "Amount must be specified")
    @ApiModelProperty(value = "Payment amount in selected currency", example = "50")
    private Long amount;

    @NotNull(message = "Currency must be specified")
    @ApiModelProperty(value = "Payment currency", example = "USD")
    private Currency currency;
}