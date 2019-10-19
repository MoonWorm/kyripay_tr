/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.infrastructure.adapter.in.payment.CustomGlobalExceptionHandler;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentTemplateResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

/**
 * @author M-ATA
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class PaymentTemplateEndpointApiTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @ClassRule
    public static final PostgreSQLContainer postgres = new PostgreSQLContainer();

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private RequestSpecification documentationSpec;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    public void createSuccess() {
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(getPaymentTemplateRequest(UUID.randomUUID().toString()))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentTemplateResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void createInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);
        assertThat(responseModel).isNotNull().hasFieldOrPropertyWithValue("status", 400);
        assertThat(responseModel.getErrors()).hasSize(1);
    }

    @Test
    public void readAllSuccess() {
        createPaymentTemplate(USER_ID, UUID.randomUUID().toString());
        createPaymentTemplate(USER_ID, UUID.randomUUID().toString());
        List<PaymentTemplateResponse> response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .param("limit", 2)
                .param("offset", 0)
                .when()
                .get("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
    }

    @Test
    public void readByIdSuccess() throws IOException, URISyntaxException {
        long paymentTemplateId = createPaymentTemplate(USER_ID, UUID.randomUUID().toString());
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .when()
                .get("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentTemplateResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void updateSuccess() {
        String templateName = UUID.randomUUID().toString();
        long paymentTemplateId = createPaymentTemplate(USER_ID, templateName);
        PaymentTemplateResponse response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(getPaymentTemplateRequest(templateName))
                .when()
                .put("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(PaymentTemplateResponse.class);
        assertThat(response).isNotNull();
    }


    @Test
    public void updateInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .put("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);
        assertThat(responseModel).isNotNull().hasFieldOrPropertyWithValue("status", 400);
        assertThat(responseModel.getErrors()).hasSize(1);
    }

    @Test
    public void deleteSuccess() {
        long paymentTemplateId = createPaymentTemplate(USER_ID, UUID.randomUUID().toString());
        given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .when()
                .delete("/api/v1/paymenttemplates/{id}", paymentTemplateId)
                .then()
                .assertThat().statusCode(SC_OK);
    }

    private String readTestResource(String relativePath) throws URISyntaxException, IOException {
        URI uri = PaymentTemplateEndpointApiTest.class.getResource(relativePath).toURI();
        Path path = Paths.get(uri);
        String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
        return resourceStr;
    }

    private long createPaymentTemplate(UUID userId, String templateName) {
        String body = getPaymentTemplateRequest(templateName);
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
        assertThat(response).isNotNull();
        return response.getId();
    }

    private String getPaymentTemplateRequest(String templateName) {
        String result = "{\n" +
                "  \"name\": \"" + templateName + "\",\n" +
                "  \"paymentDetails\": {\n" +
                "    \"amount\": {\n" +
                "      \"amount\": 50,\n" +
                "      \"currency\": \"USD\"\n" +
                "    },\n" +
                "    \"bankId\": 123,\n" +
                "    \"accountNumber\": \"12345AN\",\n" +
                "    \"recipientInfo\": {\n" +
                "      \"firstName\": \"Vasia\",\n" +
                "      \"lastName\": \"Pupkin\",\n" +
                "      \"bankUrn\": \"0000/00222/0XXXX\",\n" +
                "      \"bankName\": \"Cool Bank and Partners\",\n" +
                "      \"bankAddress\": \"Paris, Main str., 1-2\",\n" +
                "      \"accountNumber\": \"54321TTU\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        return result;
    }

}

