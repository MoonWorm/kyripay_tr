package com.kyripay.paymentworkflow.domain.service.port.out;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;

public interface ConverterAdapter {
    public void convert(Payment payment);
}
