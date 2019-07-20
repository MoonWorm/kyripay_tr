package com.kyripay.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class PaymentTemplateRequest {

    @NotBlank(message = "Template name must be specified")
    @ApiModelProperty(value = "Template name", example = "My payment for the apartment rental")
    private final String name;

    private PaymentTemplateDetails paymentDetails;

    @JsonCreator
    public PaymentTemplateRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
}