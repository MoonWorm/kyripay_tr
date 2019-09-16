package com.kyripay.paymentworkflow.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableList;
import com.kyripay.paymentworkflow.domain.dto.payment.Payment;
import com.kyripay.paymentworkflow.stream.ConverterStreams;
import com.kyripay.paymentworkflow.stream.PaymentStreams;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.KafkaContainer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.autoconfigure.exclude="
        + "org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration")
@ContextConfiguration(initializers = {PaymentWorkflowApiTest.Initializer.class})
public class PaymentWorkflowApiTest {

    @LocalServerPort
    int port;

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer();

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    PaymentStreams paymentStreams;

    @Autowired
    ConverterStreams converterStreams;

    @Test
    public void incomingPaymentShouldBeSendToConverter() throws JSONException {
        stubFor(get(urlEqualTo("/api/v1/traces"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"paymentId\": 1,\n" +
                                "  \"headers\": {\n" +
                                "    \"userId\": \"63fc53d7-3a67-4d12-8cd0-50bb2dd9a14d\"\n" +
                                "  },\n" +
                                "  \"created\": \"2019-09-14T21:54:30.074404\",\n" +
                                "  \"updated\": \"2019-09-14T21:54:30.074404\"\n" +
                                "}")));

        stubFor(get(urlEqualTo("/api/v1/payments"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"userId\": \"63fc53d7-3a67-4d12-8cd0-50bb2dd9a14d\",\n" +
                                "  \"status\": \"CREATED\",\n" +
                                "  \"paymentDetails\": {\n" +
                                "    \"amount\": {\n" +
                                "      \"amount\": 33,\n" +
                                "      \"currency\": \"USD\"\n" +
                                "    },\n" +
                                "    \"bankId\": 1,\n" +
                                "    \"accountNumber\": \"123\",\n" +
                                "    \"recipientInfo\": {\n" +
                                "      \"firstName\": \"Ivan\",\n" +
                                "      \"lastName\": \"Ivanov\",\n" +
                                "      \"bankName\": \"first bank\",\n" +
                                "      \"bankAddress\": \"Italy, Milan, Main str., 1-2\",\n" +
                                "      \"accountNumber\": \"1234567\"\n" +
                                "    }\n" +
                                "  },\n" +
                                "  \"createdOn\": 1568489717252\n" +
                                "}\n")));

        Payment payment = new Payment(11, UUID.randomUUID(), Payment.Status.CREATED, null, 111);

        kafkaTemplate.send("payment-workflow-process", 0, "1", "{\"paymentId\": 11}");
        // consumer from converter service
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        consumerProperties.put("group.id", "test");
        consumerProperties.put("key.deserializer", StringDeserializer.class);
        consumerProperties.put("value.deserializer", StringDeserializer.class);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);

        consumer.subscribe(ImmutableList.of("converter-process"));

        ConsumerRecords<String, String> messages = consumer.poll(Duration.ofSeconds(5));
        assertEquals(1, messages.count());
        String value = messages.records("converter-process").iterator().next().value();
        JSONAssert.assertEquals(
                "{\"documentId\":\"x\",\"status\":\"READY\"}",
                value,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("documentId",  new RegularExpressionValueMatcher<>(".*"))
                )
        );
    }

    @TestConfiguration
    public static class KafkaProducerConfig {

        @Bean
        public ProducerFactory<String, String> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.cloud.stream.kafka.binder.brokers=" + kafkaContainer.getBootstrapServers()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
