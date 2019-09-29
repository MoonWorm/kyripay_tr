package com.kyripay.paymentworkflow.adapter.converter;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.domain.port.out.Converter;
import com.kyripay.paymentworkflow.stream.ConverterStreams;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Service
public class ConverterImpl implements Converter {

    private MessageChannel convertPaymentChannel;

    public ConverterImpl(@Qualifier(ConverterStreams.CONVERTER_PROCESS) MessageChannel convertPaymentChannel) {
        this.convertPaymentChannel = convertPaymentChannel;
    }

    @Override
    public void convert(Payment payment) {
        convertPaymentChannel.send(new GenericMessage<>(payment));
    }
}
