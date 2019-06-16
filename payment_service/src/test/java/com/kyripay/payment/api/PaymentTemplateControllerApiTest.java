/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
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

    @Sql(statements = "DELETE FROM TEST.PAYMENT_TEMPLATE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createSuccess() throws URISyntaxException, IOException {
        ExtractableResponse<Response> response = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request.json"))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract();
        assertNotNull(response.jsonPath().get("id"));
        assertNotNull(response.jsonPath().get("paymentDetails"));
    }

    @Test
    public void createInvalid() throws IOException, URISyntaxException {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .post("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = response.as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(1));
    }


    @Sql(statements = "INSERT INTO TEST.PAYMENT_TEMPLATE (id, user_id, name, bank_id, account_number, recipient_first_name, recipient_last_name, recipient_bank_name, recipient_bank_address, recipient_bank_account, amount, currency, created_on, updated_on) VALUES (1, 1, 'Template 1', 1, '12344IBAN', 'Vasia', 'Pupkin', 'Bank 1', 'Street 1, 1', '3123123IBAN', 1000, 'BYN', '2019-05-13 07:15:31.123456789', '2019-05-13 07:15:31.123456789');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO TEST.PAYMENT_TEMPLATE (id, user_id, name, bank_id, account_number, recipient_first_name, recipient_last_name, recipient_bank_name, recipient_bank_address, recipient_bank_account, amount, currency, created_on, updated_on) VALUES (2, 1, 'Template 2', 1, '12344IBAN', 'Vasia', 'Pupkin', 'Bank 1', 'Street 1, 1', '3123123IBAN', 1000, 'BYN', '2019-05-13 07:15:31.123456789', '2019-05-13 07:15:31.123456789');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM TEST.PAYMENT_TEMPLATE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void readAllSuccess() {
        ExtractableResponse<Response> extractableResponse = given(this.documentationSpec)
                .filter(document("payment-template/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .param("limit", 3)
                .param("offset", 0)
                .when()
                .get("/api/v1/paymenttemplates")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract();
        List response = extractableResponse.body().as(List.class);
        assertNotNull(response);
        assertEquals(2, response.size());
    }


    @Sql(statements = "INSERT INTO TEST.PAYMENT_TEMPLATE (id, user_id, name, bank_id, account_number, recipient_first_name, recipient_last_name, recipient_bank_name, recipient_bank_address, recipient_bank_account, amount, currency, created_on, updated_on) VALUES (1, 1, 'Template 1', 1, '12344IBAN', 'Vasia', 'Pupkin', 'Bank 1', 'Street 1, 1', '3123123IBAN', 1000, 'BYN', '2019-05-13 07:15:31.123456789', '2019-05-13 07:15:31.123456789');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM TEST.PAYMENT_TEMPLATE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void readByIdSuccess() {
        given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .when()
                .get("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON);
    }


    @Sql(statements = "INSERT INTO TEST.PAYMENT_TEMPLATE (id, user_id, name, bank_id, account_number, recipient_first_name, recipient_last_name, recipient_bank_name, recipient_bank_address, recipient_bank_account, amount, currency, created_on, updated_on) VALUES (1, 1, 'Template 1', 1, '12344IBAN', 'Vasia', 'Pupkin', 'Bank 1', 'Street 1, 1', '3123123IBAN', 1000, 'BYN', '2019-05-13 07:15:31.123456789', '2019-05-13 07:15:31.123456789');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM TEST.PAYMENT_TEMPLATE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void updateSuccess() throws URISyntaxException, IOException {
        given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request.json"))
                .when()
                .put("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON);
    }


    @Test
    public void updateInvalid() throws IOException, URISyntaxException {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .body(readTestResource("/com/kyripay/payment/api/payment_template_request_invalid.json"))
                .when()
                .put("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = response.as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel.getStatus(), is(400));
        assertThat(responseModel.getErrors().size(), is(1));
    }

    @Sql(statements = "INSERT INTO TEST.PAYMENT_TEMPLATE (id, user_id, name, bank_id, account_number, recipient_first_name, recipient_last_name, recipient_bank_name, recipient_bank_address, recipient_bank_account, amount, currency, created_on, updated_on) VALUES (1, 1, 'Template 1', 1, '12344IBAN', 'Vasia', 'Pupkin', 'Bank 1', 'Street 1, 1', '3123123IBAN', 1000, 'BYN', '2019-05-13 07:15:31.123456789', '2019-05-13 07:15:31.123456789');", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM TEST.PAYMENT_TEMPLATE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void deleteSuccess() {
        given(this.documentationSpec)
                .filter(document("payment-template/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", 1L)
                .when()
                .delete("/api/v1/paymenttemplates/{id}", 1)
                .then()
                .assertThat().statusCode(SC_OK);
    }


    private String readTestResource(String relativePath) throws URISyntaxException, IOException {
        URI uri = PaymentTemplateControllerApiTest.class.getResource(relativePath).toURI();
        Path path = Paths.get(uri);
        String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
        return resourceStr;
    }

}