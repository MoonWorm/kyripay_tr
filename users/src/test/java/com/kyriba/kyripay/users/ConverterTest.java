package com.kyriba.kyripay.users;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConverterTest
{

    @Test
    public void documentConverted(){
        String id = given()
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body("{\n" +
                    "  \"data\": \"qwerty\"\n" +
                    "}")
                .when()
                .post("/v1/converters/F1/documents")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .extract()
                .jsonPath().get("id");

        assertNotNull(id);
    }

}
