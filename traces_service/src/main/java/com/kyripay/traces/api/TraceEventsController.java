/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.dto.representation.EventRepresentation;
import com.kyripay.traces.dto.request.EventCreationRequest;
import com.kyripay.traces.service.ResourceNotFoundException;
import com.kyripay.traces.service.TracesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


/**
 * @author M-ASI
 */
@Api(tags = "Traces API")
@RestController
public class TraceEventsController extends GenericTraceController
{

  public TraceEventsController(TracesService service)
  {
    super(service);
  }


  @ApiOperation(value = "Gets list of events for given trace")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Trace events list provided"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No events exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/events")
  public ResponseEntity<?> listEvents(@PathVariable("id") Long traceId) throws ResourceNotFoundException
  {
    Collection<EventRepresentation> events = service.listEvents(traceId);
    return events.isEmpty() ? status(HttpStatus.NO_CONTENT) : response(HttpStatus.OK, events);//TODO ret notfound
  }


  @ApiOperation(value = "Add event for given Trace")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Event created"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @PostMapping(value = "/traces/{id}/events")
  public ResponseEntity<?> addEvent(@PathVariable("id") Long traceId, @RequestBody EventCreationRequest creationRequest) throws ResourceNotFoundException
  {
    service.createEvent(traceId, creationRequest);
    return status(HttpStatus.CREATED);
  }

}
