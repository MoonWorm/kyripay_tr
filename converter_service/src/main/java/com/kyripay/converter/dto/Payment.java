package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@ApiModel(value = "Payment", description = "Payment document to be converted to the target format")
public class Payment implements Serializable
{
  @NotBlank(message = "Payment id can't be empty")
  @ApiModelProperty(value = "Unique payment id")
  private String id;
  @Valid
  @ApiModelProperty(value = "Payment sender account")
  private Account account;
  @Valid
  @ApiModelProperty(value = "The list of transactions")
  private List<Transaction> transactions;
}
