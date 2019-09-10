/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTemplateRecipientInfo {

    @ApiModelProperty(value = "Recipient first name", example = "Vasia")
    private String firstName;
    @ApiModelProperty(value = "Recipient last name", example = "Pupkin")
    private String lastName;
    @ApiModelProperty(value = "Bank URN", example = "0000/00222/0XXXX")
    private String bankUrn;
    @ApiModelProperty(value = "Recipient bank name", example = "Foo&Bar and Co Bank")
    private String bankName;
    @ApiModelProperty(value = "Recipient bank address", example = "Italy, Milan, Main str., 1-2")
    private String bankAddress;
    @ApiModelProperty(value = "Recipient account number", example = "1234567")
    private String accountNumber;

}