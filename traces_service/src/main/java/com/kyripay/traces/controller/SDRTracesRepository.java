/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.controller;

import com.kyripay.traces.domain.trace.ShortTraceProjection;
import com.kyripay.traces.domain.trace.Trace;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
 * @author M-ASI
 */
@RepositoryRestResource(excerptProjection = ShortTraceProjection.class)
public interface SDRTracesRepository extends PagingAndSortingRepository<Trace, Long>
{
}
