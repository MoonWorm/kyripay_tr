/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.domain.trace;

import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author M-ASI
 */
@Projection(name = "full", types = { Trace.class })
public interface FullTraceProjection
{
  Long getPaymentId();

  LocalDateTime getCreated();

  LocalDateTime getUpdated();

  List<Header> getHeaders(); //TODO expose as a Map (k:v), not as a list of "map entries"

  List<Event> getEvents();
}
