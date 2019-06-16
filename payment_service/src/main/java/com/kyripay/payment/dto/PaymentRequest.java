package com.kyripay.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;

@Data
@AllArgsConstructor
public class PaymentRequest {

    @Valid
    private PaymentDetails paymentDetails;

}