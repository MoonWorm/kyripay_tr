/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


/**
 * @author M-ATA
 */
@Data
@RequiredArgsConstructor
public class PaymentRecipientInfo {

    @NotBlank(message = "First name must be specified")
    @ApiModelProperty(value = "Recipient first name", example = "Vasia")
    private final String firstName;

    @NotBlank(message = "Last name must be specified")
    @ApiModelProperty(value = "Recipient last name", example = "Pupkin")
    private final String lastName;

    @NotBlank(message = "Bank URN must be specified")
    @ApiModelProperty(value = "Bank URN", example = "0000/00222/0XXXX")
    private final String bankUrn;

    @NotBlank(message = "Recipient bank name must be specified")
    @ApiModelProperty(value = "Recipient bank name", example = "Foo&Bar and Co Bank")
    private final String bankName;

    @NotBlank(message = "Recipient bank address must be specified")
    @ApiModelProperty(value = "Recipient bank address", example = "Italy, Milan, Main str., 1-2")
    private final String bankAddress;

    @NotBlank(message = "Recipient account number must be specified")
    @ApiModelProperty(value = "Recipient account number", example = "1234567")
    private final String accountNumber;

}