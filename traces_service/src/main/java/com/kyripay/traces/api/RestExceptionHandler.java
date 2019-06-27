/********************************************************************************
 * Copyright 2000 - 2019 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 ********************************************************************************/
package com.kyripay.traces.api;

import com.kyripay.traces.api.dto.SimpleErrorCollection;
import com.kyripay.traces.service.ResourceNotFoundException;
import com.kyripay.traces.service.ResourceAlreadyExistsException;
import com.kyripay.traces.service.TraceServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;


/**
 * @author M-ASI
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{


  @ExceptionHandler
  ResponseEntity<Object> handleResourceAlreadyExists(ResourceAlreadyExistsException e)
  {
    return handleTraceServiceException(e, HttpStatus.CONFLICT);
  }


  @ExceptionHandler
  ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e)
  {
    return handleTraceServiceException(e, HttpStatus.NOT_FOUND);
  }


  //All the JPA-related exceptions that may happen in Service (integrity constraints violations, etc) - are wrapped into some DataAccessException descendant. Handle them here.
  @ExceptionHandler
  ResponseEntity<Object> handleResourceNotFound(DataAccessException e)
  {
    return handleGenericException(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private ResponseEntity<Object> handleTraceServiceException(TraceServiceException e, HttpStatus httpStatus)
  {
    return handleGenericException(e, httpStatus);
  }


  private ResponseEntity<Object> handleGenericException(Exception e, HttpStatus httpStatus)
  {
    return ResponseEntity.status(httpStatus).headers(new HttpHeaders()).body(SimpleErrorCollection.single(e.getMessage()));
  }


  @ExceptionHandler
  ResponseEntity<Object> handleValidationException(ConstraintViolationException e)
  {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(new HttpHeaders()).body(processValidationError(e));
  }


  public SimpleErrorCollection processValidationError(ConstraintViolationException e)
  {
    return SimpleErrorCollection.collection(e.getConstraintViolations().stream()
        .map(cv -> String.format("%s -> %s", cv.getPropertyPath().toString(), cv.getMessage()))
        .collect(Collectors.toList()));
  }

}
