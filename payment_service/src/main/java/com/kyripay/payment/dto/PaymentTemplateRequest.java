package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class PaymentTemplateRequest {

    @NotBlank(message = "Template name must be specified")
    @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
    private String name;

    private PaymentDetails paymentDetails;

}