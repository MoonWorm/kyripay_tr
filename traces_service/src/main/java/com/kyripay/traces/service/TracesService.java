package com.kyripay.traces.service;

import com.kyripay.traces.dto.representation.EventRepresentation;
import com.kyripay.traces.dto.representation.HeaderRepresentation;
import com.kyripay.traces.dto.representation.TraceRepresentation;
import com.kyripay.traces.dto.request.EventCreationRequest;
import com.kyripay.traces.dto.request.HeaderCreationUpdateRequest;
import com.kyripay.traces.dto.request.TraceCreationRequest;
import com.kyripay.traces.dto.request.TraceUpdateRequest;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;


public interface TracesService
{
    Optional<TraceRepresentation> getTrace(long id);

    TraceRepresentation addTrace(@Valid TraceCreationRequest creationRequest) throws TraceServiceException;

    void deleteTrace(long id);

    TraceRepresentation updateTrace(long traceId, @Valid TraceUpdateRequest updateRequest, boolean patch) throws ResourceNotFoundException;

    void createEvent(long traceId, @Valid EventCreationRequest creationRequest) throws ResourceNotFoundException;

    Collection<EventRepresentation> listEvents(long traceId) throws ResourceNotFoundException;

    Optional<String> headerByName(long traceId, String headerName) throws ResourceNotFoundException;

    boolean putHeader(long traceId, @Valid HeaderCreationUpdateRequest header) throws ResourceNotFoundException;

    boolean deleteHeader(long traceId, String headerName) throws ResourceNotFoundException;
}
