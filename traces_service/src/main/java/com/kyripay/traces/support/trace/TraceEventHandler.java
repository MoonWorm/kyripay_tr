/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support.trace;

import com.kyripay.traces.domain.trace.Trace;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import javax.validation.Valid;
import java.time.LocalDateTime;


/**
 * https://www.baeldung.com/spring-data-rest-events
 *
 * A tiny sample how to handle some "REST-related" events.
 *
 * @author M-ASI
 */
@RepositoryEventHandler
public class TraceEventHandler
{


  @HandleBeforeCreate
  public void handleTraceBeforeCreate(Trace trace)
  {
    trace.setCreated(LocalDateTime.now());
  }


  @HandleBeforeSave
  public void handleTraceBeforeSave(@Valid Trace trace)
  {
    //TODO seems never called - investigate
    //Yes - it is called NOT on Entity store in DB, but on PUT / PATCH on that entity. If Trace is modified in the way other than PUT/PATCH - this method is not called.
    trace.setUpdated(LocalDateTime.now());
  }

}
