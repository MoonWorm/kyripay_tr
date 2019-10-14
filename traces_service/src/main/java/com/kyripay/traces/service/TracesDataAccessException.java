package com.kyripay.traces.service;

/**
 * It's a wrapper around JPA-related org.springframework.dao.DataAccessException.
 * Represents all non-handled unexpected data access issues happened inside TracesService.
 */
public class TracesDataAccessException extends TraceServiceException {

    public TracesDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
