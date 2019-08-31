package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.api.dto.PaymentProcessingRequest;
import com.kyripay.paymentworkflow.domain.dto.ConversionResult;
import com.kyripay.paymentworkflow.domain.service.PaymentProcessing;
import com.kyripay.paymentworkflow.stream.ConverterStreams;
import com.kyripay.paymentworkflow.stream.PaymentStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;

class PaymentApi {

    @Autowired
    private PaymentProcessing paymentProcessing;

    @StreamListener(PaymentStreams.PAYMENT_PROCESS)
    private void convertPayment(PaymentProcessingRequest processingRequest) {
        paymentProcessing.convert(processingRequest.getPaymentId());
    }

    @StreamListener(ConverterStreams.CONVERTER_NOTIFICATIONS)
    private void sendPayment(ConversionResult conversionResult) {
        paymentProcessing.send(conversionResult);
    }
}