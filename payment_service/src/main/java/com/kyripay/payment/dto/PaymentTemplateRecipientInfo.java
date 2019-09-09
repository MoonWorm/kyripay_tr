/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.dto;

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

    private String firstName;
    private String lastName;
    private String bankName;
    private String bankAddress;
    private String accountNumber;

}