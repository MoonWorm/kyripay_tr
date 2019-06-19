package com.kyripay.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @Valid
    private PaymentDetails paymentDetails;

}