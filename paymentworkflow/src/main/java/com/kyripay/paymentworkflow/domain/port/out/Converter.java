package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;

public interface Converter {
    void convert(Payment payment);
}
