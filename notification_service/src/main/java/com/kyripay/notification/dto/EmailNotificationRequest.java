/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * @author M-ATA
 */
@Data
public class EmailNotificationRequest extends GenericNotificationRequest {

    @NotBlank(message = "Template id for an email title must not be blank")
    @ApiModelProperty(value = "Template identifier that will be used for title composing. " +
            "All passed parameters can be applied as a placeholders.", example = "registration_conf_body")
    private final String titleTemplateId;

    @Email(message = "Target email must have a valid format")
    @ApiModelProperty(value = "Target email", example = "info@xyz.com")
    private final String to;

    @JsonCreator
    public EmailNotificationRequest(@Email @JsonProperty("to") String to,
                                    @NotBlank @JsonProperty("titleTemplateId") String titleTemplateId,
                                    @NotBlank @JsonProperty("bodyTemplateId") String bodyTemplateId) {
        super(bodyTemplateId);
        this.to = to;
        this.titleTemplateId = titleTemplateId;
    }

}