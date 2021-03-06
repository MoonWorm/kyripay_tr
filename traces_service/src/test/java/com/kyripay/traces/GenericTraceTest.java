/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces;

import com.kyripay.traces.service.TracesService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


/**
 * @author M-ASI
 */
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestDocs("build/generated-snippets")
public class GenericTraceTest
{
  @LocalServerPort
  private int port;

  //TODO XPI: API tests are black box, don't use service to manage data, prefer API calls instead. -> OK.
  @Autowired
  protected TracesService tracesService;

  protected RequestSpecification documentationSpec;


  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation)
  {
    RestAssured.port = port;
    this.documentationSpec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation)).build();
  }

}
