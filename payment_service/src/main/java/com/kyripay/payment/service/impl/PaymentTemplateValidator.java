package com.kyripay.payment.service.impl;

import com.kyripay.payment.domain.PaymentTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class PaymentTemplateValidator {

    public void validatePayment(@Valid PaymentTemplate payment) {
        // automate validation
    }

}
