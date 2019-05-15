/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ATA
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class PaymentTemplateControllerApiTest
{

  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

  private RequestSpecification documentationSpec;


  @Before
  public void setUp()
  {
    this.documentationSpec = new RequestSpecBuilder()
        .addFilter(documentationConfiguration(restDocumentation)).build();
  }


  @Test
  public void createSuccess() throws URISyntaxException, IOException
  {
    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("payment-template/{method-name}"))
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/payment_details.json"))
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
  public void readAllSuccess()
  {
    ExtractableResponse<Response> extractableResponse = given(this.documentationSpec)
        .filter(document("payment-template/{method-name}"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/paymenttemplates")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();
    List response = extractableResponse.body().as(List.class);
    assertNotNull(response);
    assertTrue(response.size() > 0);
  }


  @Test
  public void readByIdSuccess()
  {
    given(this.documentationSpec)
        .filter(document("payment-template/{method-name}",
            pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/paymenttemplates/{id}", 1)
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON);
  }


  @Test
  public void updateSuccess() throws URISyntaxException, IOException
  {
    given(this.documentationSpec)
        .filter(document("payment-template/{method-name}",
            pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/payment_details.json"))
        .when()
        .put("/api/v1/paymenttemplates/{id}", 1)
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON);
  }


  @Test
  public void deleteSuccess()
  {
    given(this.documentationSpec)
        .filter(document("payment-template/{method-name}",
            pathParameters(parameterWithName("id").description("Payment template unique identifier"))))
        .contentType(ContentType.JSON)
        .when()
        .delete("/api/v1/paymenttemplates/{id}", 1)
        .then()
        .assertThat().statusCode(SC_OK);
  }

  private String readTestResource(String relativePath) throws URISyntaxException, IOException
  {
    URI uri = PaymentTemplateControllerApiTest.class.getResource(relativePath).toURI();
    Path path = Paths.get(uri);
    String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
    return resourceStr;
  }

}