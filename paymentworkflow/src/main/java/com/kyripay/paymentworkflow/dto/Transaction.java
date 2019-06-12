package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Transaction {
    @NotBlank(message = "Currency can't be empty")
    @ApiModelProperty(value = "Transaction currency", example = "EUR")
    String currency;
    @NotBlank(message = "Amount can't be empty")
    @ApiModelProperty(value = "Transaction amount", example = "10.0")
    Float amount;
    @NotNull(message = "Recipient must be provided")
    @ApiModelProperty(value = "Transaction currency", dataType = "com.kyripay.paymentworkflow.dto.Recipient")
    Recipient recipient;
}

