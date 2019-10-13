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

//TODO XPI: (consider best practices for exception handling. See https://www.baeldung.com/exception-handling-for-rest-with-spring. ResponseStatusException. DefaultErrorAttributes.
/*
@xpinjection
Strange class. Usually error message is embedded into response directly or with existing Spring MVC abstractions.

@asidoruk
    "Unified" errors representation for all microservices of the app is a good idea in general (yes, it is not actually "unified" across all "Kyripay" services, but would be great)
    Existing MVC abstractions usually expose message + stacktrace which may contain sensitive data and may disclose technical details about implementation through public API which is unwanted
    Depending on Exception class and the "abstraction" that handles it - responses may be quite different -> representation is not unified.
    For example, javax.validation.ConstraintViolationException thrown by Service is handled automatically as INTERNAL_ERROR, and response contains useless stacktrace but doesn't contain list of violations. That's why I'd prefer to build error messages manually.

@xpinjection
Everything is so flexible and configurable in Spring MVC that such abstraction looks like overhead (for example, include stacktrace or not). Take a look here for basic pointers: https://www.baeldung.com/exception-handling-for-rest-with-spring

 */

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
