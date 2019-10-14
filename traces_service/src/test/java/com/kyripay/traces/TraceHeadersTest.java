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
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;


/**
 * @author M-ASI
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraceHeadersTest extends GenericTraceTest
{

  @BeforeAll
  public void setUp() throws TraceServiceException
  {
    // Create trace with headers H1, H2
    tracesService.addTrace(TraceCreationRequest.builder().paymentId(1L).headers(ImmutableMap.of("H1", "V1", "H2", "V2")).build());
  }
  
  @Test
  @Order(1)
  public void recordExistingTraceRestdoc() throws TraceServiceException
  {
    //Get it and record restdoc
    given(this.documentationSpec)
        .filter(document("traces/existing-trace-before-put-headers"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1");
  }


  @Test
  @Order(2)
  public void updateTraceWithHeaders() throws TraceServiceException
  {
    //language=JSON
    String req = "{\n" +
        "  \"headers\" : {\n" +
        "    \"H3\" : \"V3\",\n" +
        "    \"H4\" : \"V4\"\n" +
        "  }\n" +
        "}";

    //Put new headers (H2)
    given(this.documentationSpec)
        .filter(document("traces/put-trace-headers"))
        .contentType(ContentType.JSON)
        .body(req)
        .when()
        .put("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_NO_CONTENT);

    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/trace-after-put-headers"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    Map<String, String> headers = response.jsonPath().getMap("headers");
    assertEquals(2, headers.size());
    assertEquals("V3", headers.get("H3"));
    assertEquals("V4", headers.get("H4"));
  }


  @Test
  @Order(3)
  public void patchTraceWithHeaders() throws TraceServiceException
  {
    //language=JSON
    String req = "{\n" +
        "  \"headers\" : {\n" +
        "    \"H3\" : null,\n" +
        "    \"H4\" : \"V4_patched\"\n" +
        "  }\n" +
        "}";

    //Patch headers
    ExtractableResponse<Response> response  = given(this.documentationSpec)
        .filter(document("traces/patch-trace-headers"))
        .contentType(ContentType.JSON)
        .body(req)
        .when()
        .patch("/api/v1/traces/1")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    Map<String, String> headers = response.jsonPath().getMap("headers");
    assertEquals(1, headers.size());
    assertEquals("V4_patched", headers.get("H4"));
  }


  @Test
  @Order(4)
  public void addSingleHeader() throws TraceServiceException
  {
    //language=JSON
    String req = "{\n" +
        "  \"name\": \"H5\",\n" +
        "  \"value\": \"V5\"\n" +
        "}";

    //Put single header
    given(this.documentationSpec)
        .filter(document("traces/add-single-header"))
        .contentType(ContentType.JSON)
        .body(req)
        .when()
        .put("/api/v1/traces/1/headers")
        .then()
        .assertThat().statusCode(SC_CREATED);
  }

  @Test
  @Order(5)
  public void getSingleHeader() throws TraceServiceException
  {
    ExtractableResponse<Response> response  = given(this.documentationSpec)
        .filter(document("traces/get-single-header"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1/headers/H5")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    assertEquals("H5", response.jsonPath().getString("name"));
    assertEquals("V5", response.jsonPath().getString("value"));
  }


  @Test
  @Order(6)
  public void deleteSingleHeader() throws TraceServiceException
  {
    given(this.documentationSpec)
        .filter(document("traces/delete-header"))
        .when()
        .delete("/api/v1/traces/1/headers/H5")
        .then()
        .assertThat().statusCode(SC_NO_CONTENT);

    given(this.documentationSpec)
        .filter(document("traces/get-non-existent-header"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/v1/traces/1/headers/H5")
        .then()
        .assertThat().statusCode(SC_NOT_FOUND);
  }


}
