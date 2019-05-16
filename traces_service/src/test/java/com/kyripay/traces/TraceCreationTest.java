/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces;

import com.kyripay.traces.domain.trace.Trace;
import com.kyripay.traces.service.TracesService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ASI
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestDocs("build/generated-snippets")
public class TraceCreationTest
{
  @Rule
  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

  @LocalServerPort
  private int port;

  @Autowired
  private TracesService tracesService;

  private RequestSpecification documentationSpec;


  @Before
  public void setUp()
  {
    RestAssured.port = port;
    this.documentationSpec = new RequestSpecBuilder()
        .addFilter(documentationConfiguration(restDocumentation)).build();
  }


  @Test
  public void createSuccess()
  {
    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/create"))
        .contentType(ContentType.JSON)
        .body("{ \"paymentId\" : 1 }")
        .when()
        .post("/api/traces")
        .then()
        .assertThat().statusCode(SC_CREATED)
        .contentType(ContentType.JSON)
        .extract();
    System.out.println(response.body().asString());
    assertNotNull(response.jsonPath().get("paymentId"));
    assertEquals(1, response.jsonPath().getLong("paymentId"));
  }


  @Test
  public void listTraces()
  {
    //Dirty workaround - manual cleanup to be done in each test method.
    //ddl-auto=create-drop is not working for me (no cleanup between methods and even test classes. Even though I see in log "drop table XXX if exists"). To be investigated.
    tracesService.deleteAllTraces();

    Trace trace = new Trace();
    trace.setPaymentId(1);
    tracesService.addTrace(trace);

    ExtractableResponse<Response> response = given(this.documentationSpec)
        .filter(document("traces/list"))
        .contentType(ContentType.JSON)
        .when()
        .get("/api/traces")
        .then()
        .assertThat().statusCode(SC_OK)
        .contentType(ContentType.JSON)
        .extract();

    assertEquals(1, response.jsonPath().getLong("_embedded.traces[0].paymentId"));

  }


  //TODO To be continued. Need to elaborate an approach of writing "multi-step" (or "cleanup-on-each-method") integration tests with DB involved.
  //An option would be not to use H2 in tests and mock TracesService (or controllers, or whatever...), BUT:
  //some endpoints are served directly by Spring-Data-Rest (exposed by Repository) and work "automagically" - not clear what to mock.
  //So - not an option. I do believe we need complete integration tests.

}
