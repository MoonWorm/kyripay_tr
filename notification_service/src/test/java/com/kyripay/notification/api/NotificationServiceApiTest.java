/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
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

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ATA
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class NotificationServiceApiTest
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
  public void createEmailNotificationSuccess() throws IOException, URISyntaxException
  {
    given(this.documentationSpec)
        .filter(document("{method-name}"))
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/emailnotification.json"))
        .when()
        .post("/api/v1/emailnotifications")
        .then()
        .assertThat().statusCode(SC_OK);
  }

  @Test
  public void createEmailNotificationInvalid() throws IOException, URISyntaxException
  {
    Response response = given()
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/emailnotification_invalid.json"))
        .when()
        .post("/api/v1/emailnotifications")
        .then()
        .assertThat().statusCode(SC_BAD_REQUEST)
        .contentType(ContentType.JSON)
        .extract()
        .response();
    CustomGlobalExceptionHandler.ErrorsInfo responseModel = response.as(CustomGlobalExceptionHandler.ErrorsInfo.class);

    assertThat(responseModel.getStatus(), is(400));
    assertThat(responseModel.getErrors().size(), is(4));
  }

  @Test
  public void createSmsNotificationSuccess() throws IOException, URISyntaxException
  {
    given(this.documentationSpec)
        .filter(document("{method-name}"))
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/smsnotification.json"))
        .when()
        .post("/api/v1/smsnotifications")
        .then()
        .assertThat().statusCode(SC_OK);
  }

  @Test
  public void createSmsNotificationInvalid() throws IOException, URISyntaxException
  {
    Response response = given()
        .contentType(ContentType.JSON)
        .body(readTestResource("/com/kyripay/notification/api/smsnotification_invalid.json"))
        .when()
        .post("/api/v1/smsnotifications")
        .then()
        .assertThat().statusCode(SC_BAD_REQUEST)
        .contentType(ContentType.JSON)
        .extract()
        .response();
    CustomGlobalExceptionHandler.ErrorsInfo responseModel = response.as(CustomGlobalExceptionHandler.ErrorsInfo.class);

    assertThat(responseModel.getStatus(), is(400));
    assertThat(responseModel.getErrors().size(), is(3));
  }

  private String readTestResource(String relativePath) throws URISyntaxException, IOException
  {
    URI uri = NotificationServiceApiTest.class.getResource(relativePath).toURI();
    Path path = Paths.get(uri);
    String resourceStr = new String(Files.readAllBytes(path), StandardCharsets.UTF_8.name());
    return resourceStr;
  }

}