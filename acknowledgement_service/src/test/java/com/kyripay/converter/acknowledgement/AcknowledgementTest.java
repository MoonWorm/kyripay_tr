package com.kyripay.converter.acknowledgement;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcknowledgementTest {
    private RequestSpecification spec;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() {
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void acknowledgementConversion(){
        String id = given(this.spec)
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .body("{\n" +
                        "  \"format\" : \"coolAckFormat\",\n" +
                        "  \"customer\" : \"cmp11\",\n" +
                        "  \"data\" : \"someData\"\n" +
                        "}")
                .filter(document("acknowledgement"))
                .when()
                .post("/v1/acks")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .extract()
                .jsonPath().get("id");
        assertNotNull(id);
    }
}
