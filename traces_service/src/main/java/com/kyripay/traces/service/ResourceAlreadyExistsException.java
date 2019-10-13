/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.service;

/**
 * @author M-ASI
 */
public class ResourceAlreadyExistsException extends TraceServiceException
{
  public ResourceAlreadyExistsException(String resource)
  {
    super(String.format("Resource already exists: [%s]", resource));
  }


  //TODO: XPI: Usually such business exceptions are general and don't know about all resources where they could be applied. From my perspective the best place is in correcponding service because only it throws such exception for domain entity.
  public static ResourceAlreadyExistsException trace(long id)
  {
    return new ResourceAlreadyExistsException(String.format("Trace [%d]", id));
  }


  public static ResourceAlreadyExistsException header(long traceId, String headerName)
  {
    return new ResourceAlreadyExistsException(String.format("Trace [%d] / Header [%s]", traceId, headerName));
  }
}
