/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;


/**
 * @author M-ASI
 */
@Data
@RequiredArgsConstructor
public class SimpleErrorCollection
{
  private final Collection<String> errors;


  public static SimpleErrorCollection collection(Collection<String> errors)
  {
    return new SimpleErrorCollection(errors);
  }

  public static SimpleErrorCollection single(String error)
  {
    return new SimpleErrorCollection(Collections.singleton(error));
  }

}
