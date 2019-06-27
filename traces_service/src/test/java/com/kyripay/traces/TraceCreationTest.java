/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces;

import com.google.common.collect.ImmutableMap;
import com.kyripay.traces.domain.trace.Trace;
import com.kyripay.traces.dto.request.TraceCreationRequest;
import com.kyripay.traces.service.TraceServiceException;
import com.kyripay.traces.service.TracesService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ASI
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TraceCreationTest extends GenericTraceTest
{

  @Test
  public void createEmptyTraceSuccess()
  {
    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/create-without-headers"))
        .contentType(ContentType.JSON)
        .body("{ \"paymentId\" : 1 }")
        .when()
        .post("/api/v1/traces")
        .then()
        .assertThat().statusCode(SC_CREATED)
        .contentType(ContentType.JSON)
        .extract();

    assertNotNull(response.jsonPath().get("paymentId"));
    assertEquals(1, response.jsonPath().getLong("paymentId"));
  }


  @Test
  public void createTraceWithHeader()
  {
    //language=JSON
    String req = "{\n" +
        "  \"paymentId\": 1,\n" +
        "  \"headers\": {\n" +
        "    \"H1\": \"V1\"\n" +
        "  }\n" +
        "}" ;

    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/create-with-headers"))
        .contentType(ContentType.JSON)
        .body(req)
        .when()
        .post("/api/v1/traces")
        .then()
        .assertThat().statusCode(SC_CREATED)
        .contentType(ContentType.JSON)
        .extract();

    assertEquals(1, response.jsonPath().getLong("paymentId"));
    assertEquals("V1", response.jsonPath().getString("headers.H1"));
  }


  @Test
  public void getExistingTraceTest() throws TraceServiceException
  {
    tracesService.addTrace(TraceCreationRequest.builder().paymentId(1L).headers(ImmutableMap.of("H1", "V1")).build());

    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/get-existing-trace"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    assertEquals(1, response.jsonPath().getLong("paymentId"));
    assertEquals("V1", response.jsonPath().getString("headers.H1"));
  }


  @Test
  public void getNonExistentTrace() throws TraceServiceException
  {
    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/get-non-existent-trace"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_NOT_FOUND)
        .contentType(ContentType.JSON)
        .extract();
  }


  @Test
  public void deleteTraceTest() throws TraceServiceException
  {
    tracesService.addTrace(TraceCreationRequest.builder().paymentId(1L).build());

    given(this.documentationSpec)
        .filter(document("traces/delete-existing-trace"))
        .when()
        .delete("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_NO_CONTENT);

    assertFalse(tracesService.getTrace(1L).isPresent());
  }

}
