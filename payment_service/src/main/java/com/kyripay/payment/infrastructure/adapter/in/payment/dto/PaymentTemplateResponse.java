package com.kyripay.payment.infrastructure.adapter.in.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentTemplateResponse {

    @ApiModelProperty(value = "Unique identifier", example = "4")
    private final long id;

    @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
    private final String name;

    private final PaymentTemplateDetails paymentDetails;

    @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
    private final long createdOn;

}