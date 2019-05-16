/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.controller;

import com.kyripay.traces.domain.trace.Event;
import com.kyripay.traces.domain.trace.Trace;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author M-ASI
 */
@RequestMapping(value = "/api")
@RepositoryRestController
@RequiredArgsConstructor
public class TraceEventsController
{
  private final SDRTracesRepository repository;
  private final RepositoryEntityLinks repositoryEntityLinks;

  @ApiOperation(value = "Gets list of events for given trace")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Trace events list provided"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No events exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/events")
  public ResponseEntity<Resources<Resource<Event>>> listEvents(@PathVariable("id") Long traceId) {
    //TODO extract: common logic (like this) as it is very similar to TraceHeadersController. We can extract abstract "GenericNestedResourceCollectionController<Entity>"
    Trace trace = repository.findById(traceId).orElseThrow(ResourceNotFoundException::new);
    List<Resource<Event>> resourceList = trace.getEvents().stream()
        .map(header -> new Resource<>(header))
        .collect(Collectors.toList());

    if(resourceList.isEmpty())
      return ResponseEntity.noContent().build();

    Resources<Resource<Event>> resources = new Resources<>(resourceList, repositoryEntityLinks.linkToSingleResource(trace));
    return ResponseEntity.ok(resources);
  }


  @ApiOperation(value = "Add event for given Trace")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Event created"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @PostMapping(value = "/traces/{id}/events")
  public ResponseEntity<HttpStatus> addEvent(@PathVariable("id") Long traceId, @RequestBody Event event) {
    Trace trace = repository.findById(traceId).orElseThrow(ResourceNotFoundException::new);
    event.setCreated(LocalDateTime.now());
    trace.addEvent(event);
    repository.save(trace);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
