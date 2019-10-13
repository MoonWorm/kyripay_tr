package com.kyripay.payment.domain.port.in.payment.impl;

import com.kyripay.payment.domain.Payment;
import com.kyripay.payment.domain.vo.Status;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@Validated
public class PaymentValidator {

    public void validatePayment(@Valid Payment payment) {
        // automate validation
    }

    public void validatePaymentStatus(@Valid @NotNull Status payment) {
        // automate validation
    }

}
