/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
public class RecipientInfo
{

  @NotBlank(message = "First name must be specified")
  @ApiModelProperty(value = "Recipient first name", example = "Vasia")
  private String firstName;

  @NotBlank(message = "Last name must be specified")
  @ApiModelProperty(value = "Recipient last name", example = "Pupkin")
  private String lastName;

  @NotBlank(message = "Recipient bank name must be specified")
  @ApiModelProperty(value = "Recipient bank name", example = "Foo&Bar and Co Bank")
  private String bankName;

  @NotBlank(message = "Recipient bank address must be specified")
  @ApiModelProperty(value = "Recipient bank address", example = "Italy, Milan, Main str., 1-2")
  private String bankAddress;

  @NotBlank(message = "Recipient account number must be specified")
  @ApiModelProperty(value = "Recipient account number", example = "1234567")
  private String accountNumber;


  public static RecipientInfoBuilder recipientInfoBuilder()
  {
    return new RecipientInfoBuilder();
  }


  public static class RecipientInfoBuilder
  {

    private RecipientInfo instance = new RecipientInfo();


    public RecipientInfoBuilder firstName(String firstName)
    {
      instance.setFirstName(firstName);
      return this;
    }


    public RecipientInfoBuilder lastName(String lastName)
    {
      instance.setLastName(lastName);
      return this;
    }


    public RecipientInfoBuilder bankName(String bankName)
    {
      instance.setBankName(bankName);
      return this;
    }


    public RecipientInfoBuilder bankAddress(String bankAddress)
    {
      instance.setBankAddress(bankAddress);
      return this;
    }


    public RecipientInfoBuilder accountNumber(String accountNumber)
    {
      instance.setAccountNumber(accountNumber);
      return this;
    }


    public RecipientInfo build()
    {
      return instance;
    }

  }

}