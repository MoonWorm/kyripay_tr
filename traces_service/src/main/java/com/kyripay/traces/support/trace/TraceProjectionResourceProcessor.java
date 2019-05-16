/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.support.trace;

import com.kyripay.traces.support.ProjectionResourceProcessor;
import com.kyripay.traces.domain.trace.ShortTraceProjection;
import com.kyripay.traces.domain.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;


/**
 * @author M-ASI
 */
@Component
public class TraceProjectionResourceProcessor extends ProjectionResourceProcessor<Trace, ShortTraceProjection>
{
  @Autowired
  public TraceProjectionResourceProcessor(ResourceProcessor<Resource<Trace>> entityResourceProcessor)
  {
    super(entityResourceProcessor);
  }
}
