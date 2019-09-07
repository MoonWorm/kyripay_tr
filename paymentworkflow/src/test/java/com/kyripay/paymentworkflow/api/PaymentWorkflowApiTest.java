package com.kyripay.paymentworkflow.api;

import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.stream.ConverterStreams;
import com.kyripay.paymentworkflow.stream.PaymentStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "log.dirs=build/kafka-logs"})
public class PaymentWorkflowApiTest {

    @LocalServerPort
    int port;

    @Autowired
    PaymentStreams paymentStreams;

    @Autowired
    ConverterStreams converterStreams;

    @Test
    public void incomingPaymentShouldBeSendToConverter() {
        Payment payment = new Payment(1, UUID.randomUUID(), Payment.Status.CREATED, null, 111);
        paymentStreams.process().send(MessageBuilder.withPayload(payment).build());
//        Object payload = messageCollector.forChannel(converterStreams.convertPayment()).poll().getPayload().toString();
    }
}
