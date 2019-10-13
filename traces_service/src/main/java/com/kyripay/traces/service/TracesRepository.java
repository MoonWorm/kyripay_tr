/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.service;

import com.kyripay.traces.domain.trace.Trace;
import org.springframework.data.repository.CrudRepository;


/**
 * @author M-ASI
 */
public interface TracesRepository extends CrudRepository<Trace, Long>
{
}
