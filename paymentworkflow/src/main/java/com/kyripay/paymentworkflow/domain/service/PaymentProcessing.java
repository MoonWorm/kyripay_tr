package com.kyripay.paymentworkflow.domain.service;

import com.kyripay.paymentworkflow.domain.dto.ConversionResult;

public interface PaymentProcessing {
    void convert(Long paymentId);
    void send(ConversionResult conversionResult);
}
