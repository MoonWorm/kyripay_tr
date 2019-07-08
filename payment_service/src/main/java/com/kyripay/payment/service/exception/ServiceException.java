package com.kyripay.payment.service.exception;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message + " -> " + ExceptionUtils.getMessage(cause), cause);
    }

}