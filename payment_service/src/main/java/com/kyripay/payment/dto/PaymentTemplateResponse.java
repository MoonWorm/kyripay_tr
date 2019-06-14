package com.kyripay.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
public class PaymentTemplateResponse {

    @ApiModelProperty(value = "Unique identifier", example = "4")
    private Long id;

    @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
    private String name;

    private PaymentDetails paymentDetails;

}