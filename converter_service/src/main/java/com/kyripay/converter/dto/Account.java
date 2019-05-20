package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Account
{
  @ApiModelProperty(value = "Account unique id")
  private String id;
  @ApiModelProperty(value = "Bank id")
  private String bankId;
  @ApiModelProperty(value = "Number")
  private String number;
  @ApiModelProperty(value = "Currency")
  private String currency;
}
