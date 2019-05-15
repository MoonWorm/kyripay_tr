/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
public class PaymentDetails
{

  @ApiModelProperty(value = "Related user id", example = "5")
  private Long userId;

  @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
  private String name;

  @ApiModelProperty(value = "Payment currency", example = "USD")
  private Currency currency;
  @ApiModelProperty(value = "Payment amount in selected currency", example = "50")
  private Long amount;

  @ApiModelProperty(value = "Bank name that will be used for the payment", example = "General Bank Inc.")
  private String bankName;
  @ApiModelProperty(value = "Account number of the mentioned bank that will be used for the payment", example = "12345")
  private String accountNumber;

  @ApiModelProperty(value = "Which payment format should be used for the payment", example = "ISO_123")
  private String paymentFormat;
  private RecipientInfo recipientInfo;
  private ConnectionLine connectionLine;


  public static PaymentTemplateBuilder paymentDetailsBuilder()
  {
    return new PaymentTemplateBuilder();
  }


  public static class PaymentTemplateBuilder
  {

    private PaymentDetails instance = new PaymentDetails();


    public PaymentTemplateBuilder userId(Long userId)
    {
      instance.setUserId(userId);
      return this;
    }


    public PaymentTemplateBuilder name(String name)
    {
      instance.setName(name);
      return this;
    }


    public PaymentTemplateBuilder currency(Currency currency)
    {
      instance.setCurrency(currency);
      return this;
    }


    public PaymentTemplateBuilder amount(Long amount)
    {
      instance.setAmount(amount);
      return this;
    }


    public PaymentTemplateBuilder accountNumber(String accountNumber)
    {
      instance.setAccountNumber(accountNumber);
      return this;
    }


    public PaymentTemplateBuilder bankName(String bankName)
    {
      instance.setBankName(bankName);
      return this;
    }


    public PaymentTemplateBuilder paymentFormat(String paymentFormat)
    {
      instance.setPaymentFormat(paymentFormat);
      return this;
    }


    public PaymentTemplateBuilder recipientInfo(RecipientInfo recipientInfo)
    {
      instance.setRecipientInfo(recipientInfo);
      return this;
    }


    public PaymentTemplateBuilder connectionLine(ConnectionLine connectionLine)
    {
      instance.setConnectionLine(connectionLine);
      return this;
    }


    public PaymentDetails build()
    {
      return instance;
    }

  }

}