package com.kyripay.acknowledgement;

import com.kyripay.acknowledgement.dto.Acknowledgement;
import com.kyripay.acknowledgement.repository.AcknowledgementRepository;
import com.kyripay.acknowledgement.repository.ConvertedAcknowledgementRepository;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureRestDocs("build/generated-snippets")
@ContextConfiguration(initializers = AcknowledgementTest.Initializer.class)
public class AcknowledgementTest {

    @ClassRule
    public static GenericContainer mongo = new GenericContainer<>("mongo:3.6").withExposedPorts(27017);

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
    {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongo.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongo.getMappedPort(27017)
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    private RequestSpecification spec;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private AcknowledgementRepository ackRepostiory;
    @Autowired
    private ConvertedAcknowledgementRepository convertedAckRepository;

    @Before
    public void setUp() {
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void acknowledgementConversion(){
        String response = given(this.spec)
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .body("{\n" +
                        "  \"format\" : \"BASIC_ACK\",\n" +
                        "  \"customer\" : \"cmp11\",\n" +
                        "  \"data\" : \"someBase64CodedData\"\n" +
                        "}")
                .filter(document("{methodName}"))
                .when()
                .post("/api/v1/acks")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .contentType("")
                .extract()
                .body().asString();
        assertEquals("", response);
        Acknowledgement ack = new Acknowledgement();
        ack.setFormat("BASIC_ACK");
        assertNotNull(ackRepostiory.findOne(Example.of(ack)).orElse(null));
    }

    @Test
    public void acknowledgementConversionWithEmptyFormat(){
        badRequestTest("{\n" +
                             "  \"format\" : \"\",\n" +
                             "  \"customer\" : \"cmp11\",\n" +
                             "  \"data\" : \"someBase64CodedData\"\n" +
                             "}", "Format can't be empty");
    }

    @Test
    public void acknowledgementConversionWithoutFormat(){
        badRequestTest("{\n" +
                             "  \"customer\" : \"cmp11\",\n" +
                             "  \"data\" : \"someBase64CodedData\"\n" +
                             "}", "Format can't be empty");
    }

    private void badRequestTest(String body, String errorMessageExpected){
        String errMessage = (String) given(this.spec)
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .body(body)
                .filter(document("{methodName}"))
                .when()
                .post("/api/v1/acks")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .extract()
                .jsonPath().getList("errors.defaultMessage").get(0);
        assertEquals(errorMessageExpected, errMessage);
    }
}
