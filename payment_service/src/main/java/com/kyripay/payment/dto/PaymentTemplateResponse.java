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
    private long id;

    @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
    private String name;

    private PaymentDetails paymentDetails;

    @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
    private long createdOn;

}