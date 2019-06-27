/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.dto.representation.HeaderRepresentation;
import com.kyripay.traces.service.ResourceNotFoundException;
import com.kyripay.traces.service.TracesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @author M-ASI
 */
@Api(tags = "Traces API")
@RestController
public class TraceHeadersController extends GenericTraceController
{
  public TraceHeadersController(TracesService service)
  {
    super(service);
  }


  @ApiOperation(value = "Gets header value by Trace id and header name as a Plain Text (if Accept=text/plain header is present)", produces = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 200, message = "Header value is provided as text/plain"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No headers exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/headers/{header}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> headerByNameAsTextPlain(@PathVariable("id") Long traceId, @PathVariable("header") String name) throws ResourceNotFoundException
  {
    return service.headerByName(traceId, name)
        .map(h -> response(HttpStatus.OK, h))
        .orElseGet(() -> status(HttpStatus.NO_CONTENT));
  }


  @ApiOperation(value = "Gets header value by Trace id and header headerName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({
      @ApiResponse(code = 200, message = "Header value is provided as application/json"),
      @ApiResponse(code = 404, message = "Trace with given Id or header with given headerName is not found"),
  })
  @GetMapping(value = "/traces/{id}/headers/{header}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<HeaderRepresentation> headerByNameAsJson(@PathVariable("id") Long traceId, @PathVariable("header") String headerName) throws ResourceNotFoundException
  {
    return service.headerByName(traceId, headerName)
        .map(h -> response(HttpStatus.OK, HeaderRepresentation.builder().name(headerName).value(h).build()))
        .orElseThrow(() -> ResourceNotFoundException.header(traceId, headerName));
  }


  @ApiOperation(value = "Add header for given Trace")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Header created"),
      @ApiResponse(code = 204, message = "Header updated"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @PutMapping(value = "/traces/{id}/headers")
  public ResponseEntity<HttpStatus> putHeader(@PathVariable("id") Long traceId, @RequestBody HeaderRepresentation header) throws ResourceNotFoundException
  {
    boolean created = service.putHeader(traceId, header);
    return status(created ? HttpStatus.CREATED : HttpStatus.NO_CONTENT);
  }


  @ApiOperation(value = "Deletes header for given Trace and header headerName")
  @ApiResponses({
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No header with given name exist for the Trace with given Id")
  })
  @DeleteMapping(value = "/traces/{id}/headers/{header}")
  public ResponseEntity<HeaderRepresentation> deleteHeader(@PathVariable("id") Long traceId, @PathVariable("header") String headerName) throws ResourceNotFoundException
  {
    service.deleteHeader(traceId, headerName);
    return status(HttpStatus.NO_CONTENT);
  }

}
