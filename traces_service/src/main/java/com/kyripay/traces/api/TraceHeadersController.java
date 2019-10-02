/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.dto.representation.HeaderRepresentation;
import com.kyripay.traces.dto.request.HeaderCreationUpdateRequest;
import com.kyripay.traces.service.ResourceNotFoundException;
import com.kyripay.traces.service.TracesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * @author M-ASI
 */
@API_V1
@Api(tags = "Traces API")
@RestController
@RequiredArgsConstructor
public class TraceHeadersController
{
  private final TracesService service;

  @ApiOperation(value = "Gets header value by Trace id and header headerName", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({
      @ApiResponse(code = 200, message = "Header value is provided as application/json"),
      @ApiResponse(code = 404, message = "Trace with given Id or header with given headerName is not found"),
  })
  @GetMapping(value = "/traces/{id}/headers/{header}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public @ResponseBody HeaderRepresentation headerByName(@PathVariable("id") Long traceId, @PathVariable("header") String headerName) throws ResourceNotFoundException
  {
    return service.headerByName(traceId, headerName)
        .map(h -> new HeaderRepresentation(headerName, h))
        .orElseThrow(() -> ResourceNotFoundException.header(traceId, headerName));
  }


  @ApiOperation(value = "Add header for given Trace")
  @ApiResponses({
      @ApiResponse(code = 204, message = "Header created or updated"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found")
  })
  @PutMapping(value = "/traces/{id}/headers")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void putHeader(@PathVariable("id") Long traceId, @RequestBody HeaderCreationUpdateRequest header) throws ResourceNotFoundException
  {
    service.putHeader(traceId, header);
  }


  @ApiOperation(value = "Deletes header for given Trace and header headerName")
  @ApiResponses({
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "Header deleted or did not exist")
  })
  @DeleteMapping(value = "/traces/{id}/headers/{header}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteHeader(@PathVariable("id") Long traceId, @PathVariable("header") String headerName) throws ResourceNotFoundException
  {
    service.deleteHeader(traceId, headerName);
  }

}
