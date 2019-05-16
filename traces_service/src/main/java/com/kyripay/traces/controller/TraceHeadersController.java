/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.controller;

import com.kyripay.traces.domain.trace.Header;
import com.kyripay.traces.domain.trace.Trace;
import com.kyripay.traces.service.TracesService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author M-ASI
 */
@RequestMapping(value = "/api")
//TODO investigate: If I do not specify "/api" here - controller works well (and it should - because annotated with @RepositoryRestController which implies @BasePathAwareController
//which means SDR base path is already considered.) - so, if not specified - code like [resource.add(linkTo(methodOn(TraceHeadersController.class).listHeaders(resource.getContent().getId())).withRel("headers"));] does SKIP base path in the resulting URL -> it is incorrect.
@RepositoryRestController
@RequiredArgsConstructor
public class TraceHeadersController
{
  private final TracesService tracesService;

  private final RepositoryEntityLinks repositoryEntityLinks;

  @ApiOperation(value = "Gets list of headers for given trace")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Trace headers provided"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No headers exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/headers")
  public ResponseEntity<Resources<Resource<Header>>> listHeaders(@PathVariable("id") Long traceId) {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);
    List<Resource<Header>> resourceList = trace.getHeaders().stream()
        .map(header -> new Resource<>(header))
        .collect(Collectors.toList());

    if(resourceList.isEmpty())
      return ResponseEntity.noContent().build();

    Resources<Resource<Header>> resources = new Resources<>(resourceList, repositoryEntityLinks.linkToSingleResource(trace)); //add back-reference to Trace. Let it be...
    return ResponseEntity.ok(resources);
  }

  @ApiOperation(value = "Gets header value by Trace id and header name", produces = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 200, message = "Header value is provided as text/plain"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No headers exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/headers/{header}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> headerByNameAsTextPlain(@PathVariable("id") Long traceId, @PathVariable("header") String name) {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);
    return getHeader(trace, name).map(header -> ResponseEntity.ok(header.getValue())).orElseGet(() -> ResponseEntity.noContent().build());
  }

  @ApiOperation(value = "Gets header value by Trace id and header name", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiResponses({
      @ApiResponse(code = 200, message = "Header value is provided as application/json"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 204, message = "No headers exist for the Trace with given Id")
  })
  @GetMapping(value = "/traces/{id}/headers/{header}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Resource<Header>> headerByNameAsJson(@PathVariable("id") Long traceId, @PathVariable("header") String name) {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);
    return getHeader(trace, name).map(header -> ResponseEntity.ok(new Resource<>(header))).orElseGet(() -> ResponseEntity.noContent().build());
  }

  private Optional<Header> getHeader(Trace trace, String name)
  {
    return trace.getHeaders().stream().filter(h -> h.getName().equalsIgnoreCase(name)).findFirst();
  }


  @ApiOperation(value = "Add header for given Trace")
  @ApiResponses({
      @ApiResponse(code = 201, message = "Header created"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @PostMapping(value = "/traces/{id}/headers")
  public ResponseEntity<HttpStatus> addHeader(@PathVariable("id") Long traceId, @RequestBody Header header) {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);
    trace.addHeader(header);
    tracesService.update(trace);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @ApiOperation(value = "Add header for given Trace by header name as text/plain", consumes = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 201, message = "Header created"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
      @ApiResponse(code = 409, message = "Header with given name already exists"),
  })
  @PostMapping(value = "/traces/{id}/headers/{header}", consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<HttpStatus> addHeaderPlain(@PathVariable("id") Long traceId, @PathVariable("header") String header, @RequestBody String value) {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);

    if(getHeader(trace, header).isPresent())
      return ResponseEntity.status(HttpStatus.CONFLICT).build();

    trace.addHeader(new Header(header, value));
    tracesService.update(trace);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  @ApiOperation(value = "Removes header for given Trace by header name", consumes = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 204, message = "Header removed"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @DeleteMapping(value = "/traces/{id}/headers/{header}")
  public ResponseEntity<HttpStatus> deleteHeader(@PathVariable("id") Long traceId, @PathVariable("header") String name)
  {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);
    trace.removeHeader(name);
    tracesService.update(trace);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


  @ApiOperation(value = "Updates header value for given Trace by header name", consumes = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 204, message = "Header value updated"),
      @ApiResponse(code = 201, message = "Header created"),
      @ApiResponse(code = 404, message = "Trace with given Id is not found"),
  })
  @PutMapping(value = "/traces/{id}/headers/{header}", consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<HttpStatus> updateHeaderPlain(@PathVariable("id") Long traceId, @PathVariable("header") String header, @RequestBody String value)
  {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);

    if (getHeader(trace, header).isPresent()) {
      trace.updateHeader(header, value);
      tracesService.update(trace);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    else {
      trace.addHeader(new Header(header, value));
      tracesService.update(trace);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    }
  }

  @ApiOperation(value = "Updates header value for given Trace by header name", consumes = MediaType.TEXT_PLAIN_VALUE)
  @ApiResponses({
      @ApiResponse(code = 204, message = "Header value updated"),
      @ApiResponse(code = 404, message = "Trace with given Id or header with given name is not found"),//TODO that's bad. What;s the best way to differentiate?
  })
  @PatchMapping(value = "/traces/{id}/headers/{header}", consumes = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<HttpStatus> patchHeaderPlain(@PathVariable("id") Long traceId, @PathVariable("header") String header, @RequestBody String value)
  {
    Trace trace = tracesService.getTrace(traceId).orElseThrow(ResourceNotFoundException::new);

    if (getHeader(trace, header).isPresent()) {
      trace.updateHeader(header, value);
      tracesService.update(trace);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }


}
