/********************************************************************************
 * Copyright 2019 Kyriba Corp. All Rights Reserved.                             *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 *******************************************************************************/
package com.kyripay.payment.api;

import com.kyripay.payment.service.exception.ServiceException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * @author M-ATA
 */
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(toList());

        return ResponseEntity.badRequest().headers(headers).body(new ErrorsInfo(new Date(), status.value(), errors));

    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorsInfo> handlerServiceExceptions(Exception e) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ErrorsInfo(new Date(), status,
                singletonList(ExceptionUtils.getMessage(e))));
    }

    @Data
    @RequiredArgsConstructor
    public static class ErrorsInfo {
        private final Date timestamp;
        private final int status;
        private final List<String> errors;
    }

}