package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@ApiModel(value = "Account", description = "Representation of the customer account used for the payment")
public class Account
{
  @NotBlank(message = "Account id can't be empty")
  @ApiModelProperty(value = "Account unique id")
  private String id;
  @NotBlank(message = "Bank id can't be empty")
  @ApiModelProperty(value = "Bank id")
  private String bankId;
  @NotBlank(message = "Account number can't be empty")
  @ApiModelProperty(value = "Number")
  private String number;
  @NotNull(message = "Currency must be provided")
  @ApiModelProperty(value = "Currency")
  private Currency currency;
}
