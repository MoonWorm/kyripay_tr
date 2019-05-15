/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

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

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertNotNull;
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
  public void notify_success()
  {
    String id = given(this.documentationSpec)
        .filter(document("notify_success"))
        .contentType(ContentType.JSON)
        .body("{\n" +
            "  \"userId\": \"123\",\n" +
            "  \"sender\": \"CUSTOMER_SERVICE\",\n" +
            "  \"titleTemplateId\": \"payment_completed_title.twig\",\n" +
            "  \"bodyTemplateId\": \"payment_completed_body.twig\",\n" +
            "  \"parameters\": {\n" +
            "     \"firstName\": \"Vasia\",\n" +
            "     \"lastName\": \"Pupkin\"\n" +
            "  },\n" +
            "  \"type\": \"EMAIL\"\n" +
            "}")
        .when()
        .post("/api/v1/notification")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract()
        .jsonPath().get("id");
    assertNotNull(id);
  }

}