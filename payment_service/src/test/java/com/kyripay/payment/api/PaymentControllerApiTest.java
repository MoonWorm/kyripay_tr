/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.api.dto.PaymentStatus;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.dto.PaymentResponse;
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

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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
public class PaymentControllerApiTest {

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
        PaymentResponse response = given(this.documentationSpec)
                .filter(document("payment/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_request.json"))
                .when()
                .post("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentResponse.class);
        assertNotNull(response);
    }

    @Test
    public void createInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_request_invalid.json"))
                .when()
                .post("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(9));
    }

    @Test
    public void readAllSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        createPayment(userId);
        createPayment(userId);
        List<PaymentResponse> response = given(this.documentationSpec)
                .filter(document("payment/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .param("limit", 2)
                .param("offset", 0)
                .when()
                .get("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);
        assertNotNull(response);
        assertThat(response.size(), is(2));
    }

    @Test
    public void readByIdSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        long paymentId = createPayment(userId);
        PaymentResponse response = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .when()
                .get("/api/v1/payments/{id}", paymentId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentResponse.class);
        assertNotNull(response);
    }

    @Test
    public void getStatusSuccess() throws IOException, URISyntaxException {
        long userId = 1L;
        long paymentId = createPayment(userId);
        PaymentStatus responseStatus = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .when()
                .get("/api/v1/payments/{id}/status", paymentId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentStatus.class);
        assertThat(responseStatus.getStatus(), is(Status.CREATED));
    }

    @Test
    public void updateStatusSuccess() throws URISyntaxException, IOException {
        long userId = 1L;
        long paymentId = createPayment(userId);
        PaymentStatus responseStatus = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body(readTestResource("/com/kyripay/payment/api/payment_status.json"))
                .when()
                .put("/api/v1/payments/{id}/status", paymentId)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .as(PaymentStatus.class);
        assertThat(responseStatus.getStatus(), is(Status.COMPLETED));
    }


    @Test
    public void updateStatusInvalid() throws URISyntaxException, IOException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_status_invalid.json"))
                .when()
                .put("/api/v1/payments/{id}/status", 1)
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(1));
    }

    private String readTestResource(String relativePath) throws URISyntaxException, IOException {
        URI uri = PaymentControllerApiTest.class.getResource(relativePath).toURI();
        Path path = Paths.get(uri);
        String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
        return resourceStr;
    }

    private long createPayment(long userId) throws IOException, URISyntaxException {
        PaymentResponse response = given()
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body(readTestResource("/com/kyripay/payment/api/payment_request.json"))
                .when()
                .post("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentResponse.class);
        assertNotNull(response);
        return response.getId();
    }

}