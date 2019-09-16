package com.kyripay.paymentworkflow.domain.service.impl;

import com.kyripay.paymentworkflow.domain.dto.ConversionResult;
import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import com.kyripay.paymentworkflow.domain.port.out.Converter;
import com.kyripay.paymentworkflow.domain.port.out.Payments;
import com.kyripay.paymentworkflow.domain.port.out.Traces;
import com.kyripay.paymentworkflow.domain.service.PaymentProcessing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentProcessingImpl implements PaymentProcessing {

    Traces traces;
    Converter converter;
    Payments payments;

    @Override
    public void convert(Long paymentId) {
        Map<String, String> headers = new HashMap<>();
        Event event = new Event("startProcessing", "payment-workfloq",
                Event.Type.INFO, "", LocalDateTime.now());
        traces.createTrace(headers, event);
        Payment payment = payments.getPaymentById(paymentId);
        converter.convert(payment);
    }

    @Override
    public void send(ConversionResult conversionResult) {

    }
}
