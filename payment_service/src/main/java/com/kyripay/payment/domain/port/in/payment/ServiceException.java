package com.kyripay.payment.domain.port.in.payment;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message + " -> " + ExceptionUtils.getMessage(cause), cause);
    }

}