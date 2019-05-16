/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.domain.trace;

import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;


/**
 * Headers and Events collections mapped in Trace as ElementCollection -> as they are "embeddables" they do not have own Repositories ->
 * SDR doesn't generate links to these collections, but embeds all the items into Trace. That's what we want to avoid.
 * This projection is intended to be used as "excerpt" for Trace Repository
 * @author M-ASI
 */
@Projection(name = "short", types = {Trace.class})
public interface ShortTraceProjection
{
  Long getPaymentId();

  LocalDateTime getCreated();

  LocalDateTime getUpdated();

  //TODO here we can add some other useful information in compact form (number of headers / events, last event timestamp/status or even the entire event...)
}
