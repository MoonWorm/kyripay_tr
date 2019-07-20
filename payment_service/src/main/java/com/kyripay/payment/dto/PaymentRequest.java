package com.kyripay.payment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;

@Data
public class PaymentRequest {

    @Valid
    private final PaymentDetails paymentDetails;

    @JsonCreator
    public PaymentRequest(@JsonProperty("paymentDetails") PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

}