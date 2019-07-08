/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.dto.PaymentTemplateResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ATA
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT, value = "eureka.client.enabled=false")
public class PaymentTemplateControllerApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private RequestSpecification documentationSpec;

    @Before
    public void setUp() {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    public void createSuccess() throws URISyntaxException, IOException {
        long userId = 1L;
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body(readPaymentTemplateRequest(UUID.randomUUID().toString()))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentTemplateResponse.class);
        assertNotNull(response);
    }

    @Test
    public void createInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);
        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(1));
    }

    @Test
    public void readAllSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        createPaymentTemplate(userId, UUID.randomUUID().toString());
        createPaymentTemplate(userId, UUID.randomUUID().toString());
        List<PaymentTemplateResponse> response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .param("limit", 2)
                .param("offset", 0)
                .when()
                .get("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);
        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    public void readByIdSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        long paymentTemplateId = createPaymentTemplate(userId, UUID.randomUUID().toString());
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .when()
                .get("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentTemplateResponse.class);
        assertNotNull(response);
    }

    @Test
    public void updateSuccess() throws URISyntaxException, IOException {
        long userId = 1L;
        String templateName = UUID.randomUUID().toString();
        long paymentTemplateId = createPaymentTemplate(userId, templateName);
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body(readPaymentTemplateRequest(templateName))
                .when()
                .put("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(PaymentTemplateResponse.class);
        assertNotNull(response);
    }


    @Test
    public void updateInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .put("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(1));
    }

    @Test
    public void deleteSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        long paymentTemplateId = createPaymentTemplate(userId, UUID.randomUUID().toString());
        given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .when()
                .delete("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK);
    }

    private String readPaymentTemplateRequest(String templateName) throws URISyntaxException, IOException {
        String json = readTestResource("/com/kyripay/payment/api/payment_template_request.json");
        json = json.replaceAll("<TEMPLATE_NAME>", templateName);
        return json;
    }

    private String readTestResource(String relativePath) throws URISyntaxException, IOException {
        URI uri = PaymentTemplateControllerApiTest.class.getResource(relativePath).toURI();
        Path path = Paths.get(uri);
        String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
        return resourceStr;
    }

    private long createPaymentTemplate(long userId, String templateName) throws URISyntaxException, IOException {
        String body = readPaymentTemplateRequest(templateName);
        PaymentTemplateResponse response = given()
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body(body)
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentTemplateResponse.class);
        assertNotNull(response);
        return response.getId();
    }

}