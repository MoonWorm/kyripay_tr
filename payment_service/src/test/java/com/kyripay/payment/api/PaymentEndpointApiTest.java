/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.RegexPattern;
import com.kyripay.payment.domain.vo.Status;
import com.kyripay.payment.infrastructure.adapter.in.payment.CustomGlobalExceptionHandler;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentResponse;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentStatus;
import com.kyripay.payment.infrastructure.adapter.in.payment.dto.PaymentWithUserIdResponse;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
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
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class PaymentEndpointApiTest {

    private static final UUID USER_ID = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer();

    private static WireMockServer wireMockServer;

    @LocalServerPort
    private int port;

    private RequestSpecification documentationSpec;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        RestAssured.port = port;
        this.documentationSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
                .build();
        setupStubs();
    }

    private void setupStubs() {
        wireMockServer.stubFor(post(urlEqualTo("/api/v1/traces"))
                .withRequestBody(new RegexPattern("\\{\"paymentId\":\\d+,\"headers\":\\{\"userId\":\"" + USER_ID + "\"\\}\\}"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SC_CREATED)
                        .withHeader("Content-Type", "application/json")));
    }

    @BeforeAll
    static void globalSetup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
    }

    @AfterAll
    static void globalShutdown() {
        wireMockServer.stop();
    }


    @Test
    public void createSuccess() throws URISyntaxException, IOException {
        PaymentResponse response = given(this.documentationSpec)
                .filter(document("payment/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_request.json"))
                .when()
                .post("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void createInvalid() throws IOException, URISyntaxException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_request_invalid.json"))
                .when()
                .post("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);

        assertThat(responseModel).isNotNull().hasFieldOrPropertyWithValue("status", 400);
        assertThat(responseModel.getErrors()).hasSize(9);
    }

    @Test
    public void readAllSuccess() throws IOException, URISyntaxException {
        createPayment(USER_ID);
        createPayment(USER_ID);
        List<PaymentResponse> response = given(this.documentationSpec)
                .filter(document("payment/{method-name}"))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .param("limit", 2)
                .param("offset", 0)
                .when()
                .get("/api/v1/payments")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);
        assertThat(response).isNotNull().hasSize(2);
    }

    @Test
    public void searchSuccess() throws IOException, URISyntaxException {
        long payment1Id = createPayment(USER_ID);
        long payment2Id = createPayment(USER_ID);
        updateStatus(USER_ID, payment1Id, Status.PROCESSING);
        updateStatus(USER_ID, payment2Id, Status.COMPLETED);
        List<PaymentWithUserIdResponse> processing = given(this.documentationSpec)
                .filter(document("payment/{method-name}"))
                .contentType(ContentType.JSON)
                .param("limit", 2)
                .param("offset", 0)
                .param("status", Status.PROCESSING)
                .when()
                .get("/api/v1/payments/search/result")
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(List.class);
        assertThat(processing).isNotNull().hasSize(1);
    }

    @Test
    public void readByIdSuccess() throws IOException, URISyntaxException {
        long paymentId = createPayment(USER_ID);
        PaymentResponse response = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .when()
                .get("/api/v1/payments/{id}", paymentId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentResponse.class);
        assertThat(response).isNotNull();
    }

    @Test
    public void getStatusSuccess() throws IOException, URISyntaxException {
        long paymentId = createPayment(USER_ID);
        PaymentStatus responseStatus = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .when()
                .get("/api/v1/payments/{id}/status", paymentId)
                .then()
                .assertThat().statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentStatus.class);
        assertThat(responseStatus).isNotNull().hasFieldOrPropertyWithValue("status", Status.CREATED);
    }

    @Test
    public void updateStatusSuccess() throws URISyntaxException, IOException {
        long paymentId = createPayment(USER_ID);
        PaymentStatus responseStatus = given(this.documentationSpec)
                .filter(document("payment/{method-name}",
                        pathParameters(parameterWithName("id").description("Payment unique identifier"))))
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_status.json"))
                .when()
                .put("/api/v1/payments/{id}/status", paymentId)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .as(PaymentStatus.class);
        assertThat(responseStatus).isNotNull().hasFieldOrPropertyWithValue("status", Status.COMPLETED);
    }


    @Test
    public void updateStatusInvalid() throws URISyntaxException, IOException {
        CustomGlobalExceptionHandler.ErrorsInfo responseModel = given()
                .contentType(ContentType.JSON)
                .header("userId", USER_ID)
                .body(readTestResource("/com/kyripay/payment/api/payment_status_invalid.json"))
                .when()
                .put("/api/v1/payments/{id}/status", 1)
                .then()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .extract()
                .response()
                .as(CustomGlobalExceptionHandler.ErrorsInfo.class);
        assertThat(responseModel).isNotNull().hasFieldOrPropertyWithValue("status", 400);
        assertThat(responseModel.getErrors()).hasSize(1);
    }

    private String readTestResource(String relativePath) throws URISyntaxException, IOException {
        URI uri = PaymentEndpointApiTest.class.getResource(relativePath).toURI();
        Path path = Paths.get(uri);
        String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
        return resourceStr;
    }

    private long createPayment(UUID userId) throws IOException, URISyntaxException {
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
        assertThat(response).isNotNull();
        return response.getId();
    }

    private Status updateStatus(UUID userId, long payment1Id, Status status) {
        PaymentStatus response = given()
                .contentType(ContentType.JSON)
                .header("userId", userId)
                .body("{\"status\": \"" + status.name() + "\"}")
                .when()
                .put("/api/v1/payments/{paymentId}/status", payment1Id)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .contentType(ContentType.JSON)
                .extract()
                .as(PaymentStatus.class);
        assertThat(response).isNotNull();
        return response.getStatus();

    }

    @TestConfiguration
    public static class LocalRibbonClientConfiguration {
        @Bean
        public ServerList<Server> ribbonServerList() {
            return new StaticServerList<>(new Server("localhost", wireMockServer.port()));
        }
    }

}