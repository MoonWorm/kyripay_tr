/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author M-ATA
 */
@Data
public class SmsNotificationRequest extends GenericNotificationRequest {

    @NotBlank
    private String mobileNumber;

    public SmsNotificationRequest(@NotBlank String bodyTemplateId, @NotBlank String mobileNumber) {
        super(bodyTemplateId);
        this.mobileNumber = mobileNumber;
    }

}