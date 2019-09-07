package com.kyripay.converter.api;

import com.kyripay.converter.repository.DocumentRepository;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.RegularExpressionValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.cloud.stream.test.binder.TestSupportBinderAutoConfiguration;
import org.springframework.cloud.stream.test.binder.TestSupportBinderConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder.mongoDb;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs("build/generated-snippets")
@ContextConfiguration(initializers = ConverterApiTest.Initializer.class)
@ActiveProfiles("api-test")
public class ConverterApiTest
{

    @ClassRule
    public static GenericContainer mongo = new GenericContainer<>("mongo:3.6").withExposedPorts(27017);

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer();

    @Rule
    public MongoDbRule mongoDbRule = new MongoDbRule(
        mongoDb().databaseName("converter").port(mongo.getMappedPort(27017)).build()
    );

    @LocalServerPort
    int port;


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
    {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext)
        {
            TestPropertyValues values = TestPropertyValues.of(
                "spring.data.mongodb.host=" + mongo.getContainerIpAddress(),
                "spring.data.mongodb.port=" + mongo.getMappedPort(27017),
                "spring.cloud.stream.kafka.binder.brokers=" + kafka.getBootstrapServers()
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;
    @Autowired
    private DocumentRepository repository;

    @Autowired
    SubscribableChannel input;


    @Before
    public void setUp()
    {
        this.spec = new RequestSpecBuilder().addFilter(
            documentationConfiguration(this.restDocumentation))
            .build();
    }


    @Test
    public void convertDocument_MQ() throws Exception
    {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", kafka.getBootstrapServers());
        producerProperties.put("key.serializer", StringSerializer.class);
        producerProperties.put("value.serializer", StringSerializer.class);
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);

        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", kafka.getBootstrapServers());
        consumerProperties.put("group.id", "test");
        consumerProperties.put("key.deserializer", StringDeserializer.class);
        consumerProperties.put("value.deserializer", StringDeserializer.class);
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(consumerProperties);

        consumer.subscribe(ImmutableList.of("converter-notifications"));

        producer.send(new ProducerRecord<>(
            "converter-process",
            new String(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes())
        ));


        ConsumerRecords<Object, Object> messages = consumer.poll(Duration.ofMinutes(1));
        assertEquals(1, messages.count());
        String value = (String) messages.records("converter-notifications").iterator().next().value();
        JSONAssert.assertEquals(
            "{\"documentId\":\"x\",\"status\":\"READY\"}",
            value,
            new CustomComparator(
                JSONCompareMode.STRICT,
                new Customization("documentId",  new RegularExpressionValueMatcher<>(".*"))
            )
        );
    }


    @Test
    public void convertDocument_REST() throws Exception
    {
        String id = given(this.spec)
            .port(port)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes())
            .filter(document("convert"))
            .when()
            .post("/api/v1/converters/conversion-requests")
            .then()
            .statusCode(HttpStatus.SC_ACCEPTED)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .extract()
            .jsonPath().get("documentId");

        assertNotNull(id);
        assertNotNull(repository.findById(id).orElse(null));
    }


    @Test
    public void getFormats()
    {
        String body = given(this.spec)
            .port(port)
            .filter(document("getFormats"))
            .when()
            .get("/api/v1/converters")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .extract().body().asString();

        assertNotNull(body);
    }


    @Test
    @UsingDataSet(locations = "/testdata/db/expectedData.json")
    public void getDocument() throws IOException
    {
        String data = given(this.spec)
            .port(port)
            .filter(document("getDocument", pathParameters(
                parameterWithName("id").description("Converted document id")
            )))
            .when()
            .get("/api/v1/documents/{id}", "1")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .extract().asString();

        assertEquals(
            new String(getClass().getResourceAsStream("/testdata/getDocumentResponse.json").readAllBytes()),
            data
        );
    }
}
