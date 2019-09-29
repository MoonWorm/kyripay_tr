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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentProcessingImpl implements PaymentProcessing {

    private Traces traces;
    private Converter converter;
    private Payments payments;

    @Autowired
    public PaymentProcessingImpl(Traces traces, Converter converter, Payments payments) {
        this.traces = traces;
        this.converter = converter;
        this.payments = payments;
    }

    @Override
    public void convert(Long paymentId) {
        Map<String, String> headers = new HashMap<>();
        Event event = new Event("startProcessing", "payment-workfloq",
                Event.Type.INFO, "", LocalDateTime.now());
        Trace trace = traces.createTrace(headers, event);
        Optional<Payment> payment = payments.getPaymentById(paymentId);
        if (payment.isPresent()) {
            converter.convert(payment.get());
        } else {
            traces.addEvent(
                    trace.getPaymentId(),
                    new Event("convert", "payment_workflow", Event.Type.ERROR,
                            "payment not found", LocalDateTime.now()));
        }
    }

    @Override
    public void send(ConversionResult conversionResult) {

    }
}
