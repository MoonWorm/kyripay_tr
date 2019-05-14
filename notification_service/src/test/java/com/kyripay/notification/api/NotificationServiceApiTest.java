/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.notification.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;


/**
 * @author M-ATA
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class NotificationServiceApiTest
{

  @Test
  public void notify_success()
  {
    String id = given()
        .contentType(APPLICATION_JSON_UTF8_VALUE)
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
          .statusCode(SC_OK)
          .contentType(APPLICATION_JSON_UTF8_VALUE)
          .extract()
            .jsonPath().get("id");
    assertNotNull(id);
  }

}