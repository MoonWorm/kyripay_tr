package com.kyripay.converter.api;

import com.kyripay.converter.dto.Document;
import com.kyripay.converter.dto.DocumentStatus;
import com.kyripay.converter.repository.DocumentRepository;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureRestDocs("build/generated-snippets")
@ContextConfiguration(initializers = ConverterApiTest.Initializer.class)
@ActiveProfiles("test")
public class ConverterApiTest
{

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

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;
    @Autowired
    private DocumentRepository repository;

    @Before
    public void setUp() {
        this.spec = new RequestSpecBuilder().addFilter(
            documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    public void convertDocument() throws Exception{
        String id = given(this.spec)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(getClass().getResourceAsStream("/testdata/okPayment.json").readAllBytes())
                .filter(document("convert", pathParameters(
                    parameterWithName("formatId").description("Target format id")
                )))
                .when()
                .post("/api/v1/converters/{formatId}/conversion-requests", "IDENTITY")
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .extract()
                .jsonPath().get("documentId");

        assertNotNull(id);
        assertNotNull(repository.findById(id).orElse(null));
    }

    @Test
    public void convertDocumentWithoutAccountId() throws IOException
    {
        String errorMessage = (String) given(this.spec)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .body(getClass().getResourceAsStream("/testdata/paymentWithoutAccountId.json").readAllBytes())
            .when()
            .post("/api/v1/converters/{formatId}/conversion-requests", "FORMAT_1")
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .extract()
            .jsonPath().getList("errors.defaultMessage").get(0);

        assertEquals("Account id can't be empty", errorMessage);
    }


    @Test
    public void getFormats(){
        String body = given(this.spec)
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
    public void getDocument(){

        repository.save(new Document("ID", "test", DocumentStatus.READY, "test".getBytes()));

        String data = given(this.spec)
            .body("test data".getBytes())
            .filter(document("getDocument", pathParameters(
                parameterWithName("id").description("Converted document id")
            )))
            .when()
            .get("/api/v1/documents/{id}", "ID")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .extract()
            .jsonPath().get("data");

        assertEquals("test", new String(Base64.getDecoder().decode(data)));
    }
}