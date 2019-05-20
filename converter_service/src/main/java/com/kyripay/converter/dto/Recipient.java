package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Recipient {
  @ApiModelProperty(value = "Recipient unique id (UUID)")
  private String id;
  @ApiModelProperty(value = "Recipient first name")
  private String firstName;
  @ApiModelProperty(value = "Recipient last name")
  private String lastName;
  @ApiModelProperty(value = "Bank name")
  private String bankName;
  @ApiModelProperty(value = "Bank address")
  private String bankAddress;
  @ApiModelProperty(value = "Account number")
  private String accountNumber;
}
