package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@ApiModel(value = "Transaction", description = "Single transaction description")
public class Transaction {
  @ApiModelProperty("Transaction currency. If empty, account currency is used")
  String currency;
  @Positive(message = "Transaction amount should be more than 0")
  @ApiModelProperty("Transaction amount")
  BigDecimal amount;
  @Valid
  @ApiModelProperty("Transaction recipient")
  Recipient recipient;
}