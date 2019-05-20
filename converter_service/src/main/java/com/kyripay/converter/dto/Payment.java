package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class Payment {
  @ApiModelProperty(value = "Unique payment id")
  private String id;
  @ApiModelProperty(value = "Payment sender account")
  private Account account;
  @ApiModelProperty(value = "The list of transactions")
  private List<Transaction> transactions;
}
