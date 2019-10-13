/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.service;

/**
 * @author M-ASI
 */
public class TraceServiceException extends RuntimeException
{
  public TraceServiceException(String message)
  {
    super(message);
  }


  public TraceServiceException(String message, Throwable cause)
  {
    super(message, cause);
  }


  public TraceServiceException(Throwable cause)
  {
    super(cause);
  }
}
