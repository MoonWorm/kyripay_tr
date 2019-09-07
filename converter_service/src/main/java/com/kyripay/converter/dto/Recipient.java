package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@NoArgsConstructor
@ApiModel(value = "Recipient", description = "Payment recipient")
public class Recipient  implements Serializable
{
  @NotBlank(message = "Recipient id can't be empty")
  @ApiModelProperty(value = "Recipient unique id")
  private String id;
  @ApiModelProperty(value = "Recipient first name")
  private String firstName;
  @ApiModelProperty(value = "Recipient last name")
  private String lastName;
  @NotBlank(message = "Recipient bank name can't be empty")
  @ApiModelProperty(value = "Bank name")
  private String bankName;
  @ApiModelProperty(value = "Bank address")
  private String bankAddress;
  @NotBlank(message = "Recipient account number can't be empty")
  @ApiModelProperty(value = "Account number")
  private String accountNumber;
}
