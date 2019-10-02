/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.dto.representation.TraceRepresentation;
import com.kyripay.traces.dto.request.TraceCreationRequest;
import com.kyripay.traces.dto.request.TraceUpdateRequest;
import com.kyripay.traces.service.ResourceNotFoundException;
import com.kyripay.traces.service.TracesService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.apache.http.HttpStatus.*;


/**
 * @author M-ASI
 */
@API_V1
@Api(value = "Traces", tags = "Traces API")
@RestController
@RequiredArgsConstructor
public class TraceController
{
  private final TracesService service;

  @PostMapping("/traces")
  @ApiOperation(value = "Add a new Trace", response = TraceRepresentation.class)
  @ApiResponses({
      @ApiResponse(code = SC_CREATED, message = "Trace created"),
      @ApiResponse(code = SC_CONFLICT, message = "Trace with given Id already exist"),
  })
  @ResponseStatus(HttpStatus.CREATED)
  public void addTrace(@RequestBody TraceCreationRequest creationRequest)
  {
    service.addTrace(creationRequest);
   }


  @GetMapping("/traces/{id}")
  @ApiOperation(value = "Get Trace by Payment ID", response = TraceRepresentation.class)
  @ApiParam(name = "id", value = "Payment ID", required = true, example = "123", type = "Long")
  @ApiResponses({
      @ApiResponse(code = SC_OK, message = "Trace returned"),
      @ApiResponse(code = SC_NOT_FOUND, message = "Trace with given Id is not found"),
  })
  public @ResponseBody TraceRepresentation getTrace(@PathVariable("id") long id)
  {
    return service.getTrace(id).orElseThrow(() -> ResourceNotFoundException.trace(id));
  }


  @PutMapping("/traces/{id}")
  @ApiOperation(value = "Update the entire Trace by Payment ID", response = TraceRepresentation.class)
  @ApiParam(name = "id", value = "Payment ID", required = true, example = "123", type = "Long")
  @ApiResponses({
      @ApiResponse(code = SC_NO_CONTENT, message = "Trace updated"),
      @ApiResponse(code = SC_NOT_FOUND, message = "Trace with given Id is not found"),
  })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void putTrace(@PathVariable("id") long id, @RequestBody TraceUpdateRequest traceUpdateRequest)
  {
    service.updateTrace(id, traceUpdateRequest, false);
  }


  @PatchMapping("/traces/{id}")
  @ApiOperation(value = "Update Trace by Payment ID", response = TraceRepresentation.class)
  @ApiParam(name = "id", value = "Payment ID", required = true, example = "123", type = "Long")
  @ApiResponses({
      @ApiResponse(code = SC_OK, message = "Trace returned"),
      @ApiResponse(code = SC_NOT_FOUND, message = "Trace with given Id is not found"),
  })
  public @ResponseBody TraceRepresentation patchTrace(@PathVariable("id") long id, @RequestBody TraceUpdateRequest traceUpdateRequest)
  {
    return service.updateTrace(id, traceUpdateRequest, true);
  }


  @DeleteMapping("/traces/{id}")
  @ApiOperation(value = "Delete Trace by Payment ID")
  @ApiParam(name = "id", value = "Payment ID", required = true, example = "123", type = "Long")
  @ApiResponses({
      @ApiResponse(code = SC_NO_CONTENT, message = "Trace deleted"),
      @ApiResponse(code = SC_NOT_FOUND, message = "Trace with given Id is not found"),
  })
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTrace(@PathVariable("id") long id)
  {
    service.deleteTrace(id);
  }
}

