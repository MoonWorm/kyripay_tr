package com.kyripay.converter.acknowledgement;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcknowledgementTest {

    @Test
    public void acknowledgementConversion(){
        String id = given()
                .contentType(APPLICATION_JSON_UTF8_VALUE)
                .body("{\n" +
                        "  \"format\" : \"coolAckFormat\",\n" +
                        "  \"customer\" : \"cmp11\",\n" +
                        "  \"data\" : \"someData\"\n" +
                        "}")
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
