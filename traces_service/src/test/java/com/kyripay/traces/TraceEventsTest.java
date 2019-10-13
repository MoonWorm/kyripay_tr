/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces;

import com.google.common.collect.ImmutableMap;
import com.kyripay.traces.dto.request.TraceCreationRequest;
import com.kyripay.traces.service.TraceServiceException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


/**
 * @author M-ASI
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraceEventsTest extends GenericTraceTest
{
  @BeforeAll
  public void setUp() throws TraceServiceException
  {
    // Create trace with headers H1, H2
    tracesService.addTrace(TraceCreationRequest.builder().paymentId(1L).headers(ImmutableMap.of("H1", "V1", "H2", "V2")).build());
  }

  @Test
  @Order(1)
  public void ensureNoEventsByDefult() {
    given(this.documentationSpec)
        .filter(document("traces/existing-trace-with-no-events"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1/events")
        .then()
        .assertThat().statusCode(SC_NO_CONTENT);
  }


  @Test
  @Order(2)
  public void addEvent() {
    String event = "{\n" +
        "  \"name\" : \"evt1_name\",\n" +
        "  \"source\" : \"Payment service\",\n" +
        "  \"type\" : \"INFO\",\n" +
        "  \"comment\" : \"Trace created\"\n" +
        "}";

    given(this.documentationSpec)
        .filter(document("traces/add-event"))
        .contentType(ContentType.JSON)
        .body(event)
        .when()
        .post("/api/v1/traces/1/events")
        .then()
        .assertThat().statusCode(SC_CREATED);
  }

  @Test
  @Order(3)
  public void listEvents() {
    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/list-events"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1/events")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    JsonPath jsonPath = response.jsonPath();
    assertEquals(1, jsonPath.getInt("size()"));
    assertEquals("evt1_name", jsonPath.getString("[0].name"));
    assertEquals("Payment service", jsonPath.getString("[0].source"));
    assertEquals("INFO", jsonPath.getString("[0].type"));
    assertEquals("Trace created", jsonPath.getString("[0].comment"));
  }


}
