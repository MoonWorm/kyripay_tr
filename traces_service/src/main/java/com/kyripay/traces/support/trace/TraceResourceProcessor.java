/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support.trace;

import com.kyripay.traces.support.TracesLogger;
import com.kyripay.traces.controller.TraceEventsController;
import com.kyripay.traces.controller.TraceHeadersController;
import com.kyripay.traces.domain.trace.Trace;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * @author M-ASI
 */

@Component
public class TraceResourceProcessor implements ResourceProcessor<Resource<Trace>>
{

  @Override
  public Resource<Trace> process(Resource<Trace> resource)
  {
    resource.add(linkTo(methodOn(TraceHeadersController.class).listHeaders(resource.getContent().getId())).withRel("headers"));
    resource.add(linkTo(methodOn(TraceEventsController.class).listEvents(resource.getContent().getId())).withRel("events"));
    return resource;
  }
}
