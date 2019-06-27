/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.api.dto.SimpleErrorCollection;
import com.kyripay.traces.service.TracesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author M-ASI
 */
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class GenericTraceController
{

  protected final TracesService service;


  protected <T> ResponseEntity<T> response(HttpStatus httpStatus, T entity)
  {
    return ResponseEntity.status(httpStatus).body(entity);
  }


  protected <T> ResponseEntity<T> status(HttpStatus httpStatus)
  {
    return ResponseEntity.status(httpStatus).build();
  }


  protected ResponseEntity<?> error(HttpStatus httpStatus, String errorMessage)
  {
    return ResponseEntity.status(httpStatus).body(SimpleErrorCollection.single(errorMessage));
  }

}
