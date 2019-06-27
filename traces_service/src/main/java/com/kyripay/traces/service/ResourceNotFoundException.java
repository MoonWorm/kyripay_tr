/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.service;


/**
 * @author M-ASI
 */
public class ResourceNotFoundException extends TraceServiceException
{
  public ResourceNotFoundException(String resourceName)
  {
    super(String.format("Resource not found: %s", resourceName));
  }


  public static ResourceNotFoundException trace(long id)
  {
    return new ResourceNotFoundException(String.format("Trace [%d]", id));
  }

  public static ResourceNotFoundException header(long traceId, String headerName)
  {
    return new ResourceNotFoundException(String.format("Trace [%d] / Header [%s]", traceId, headerName));
  }
}
