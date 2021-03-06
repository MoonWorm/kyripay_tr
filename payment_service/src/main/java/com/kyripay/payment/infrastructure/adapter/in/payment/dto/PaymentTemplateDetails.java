/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.infrastructure.adapter.in.payment.dto;

import com.kyripay.payment.domain.vo.Amount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author M-ATA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTemplateDetails {

    private Amount amount;
    private Long bankId;
    private String accountNumber;
    private PaymentTemplateRecipientInfo recipientInfo;

}