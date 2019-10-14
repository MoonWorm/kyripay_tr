package com.kyripay.traces.service.impl;

import com.kyripay.traces.domain.trace.Event;
import com.kyripay.traces.domain.trace.Trace;
import com.kyripay.traces.dto.representation.EventRepresentation;
import com.kyripay.traces.dto.representation.HeaderRepresentation;
import com.kyripay.traces.dto.representation.TraceRepresentation;
import com.kyripay.traces.dto.request.EventCreationRequest;
import com.kyripay.traces.dto.request.HeaderCreationUpdateRequest;
import com.kyripay.traces.dto.request.TraceCreationRequest;
import com.kyripay.traces.dto.request.TraceUpdateRequest;
import com.kyripay.traces.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Validated
public class TracesServiceImpl implements TracesService {

    private final TracesRepository repo;


    @Override
    @Transactional(readOnly = true)
    public Optional<TraceRepresentation> getTrace(long id) {
        return repo.findById(id).map(Trace::toRepresentation);
    }


    @Override
    @Transactional
    public TraceRepresentation addTrace(@Valid TraceCreationRequest creationRequest) throws TraceServiceException {
        if (repo.existsById(creationRequest.getPaymentId()))
            throw ResourceAlreadyExistsException.trace(creationRequest.getPaymentId());
        return repo.save(Trace.fromCreationRequest(creationRequest)).toRepresentation();
    }


    @Override
    @Transactional
    public void deleteTrace(long id) {
        //TODO: XPI: Just apply delete to avoid double requesting. Deletion is idempotent operation. -> repo.deleteById(id) throws some unchecked exception (if no such entity) -> Then it seems better to catch exception thrown by JPA repository implementation or create your own 'optimized' method in repository.
        if (repo.existsById(id))
            repo.deleteById(id);
    }


    @Override
    @Transactional
    public TraceRepresentation updateTrace(long traceId, @Valid TraceUpdateRequest updateRequest, boolean patch) throws ResourceNotFoundException {
        Trace trace = repo.findById(traceId)
                .map(t -> patch ? patch(t, updateRequest) : update(t, updateRequest))
                .orElseThrow(() -> ResourceNotFoundException.trace(traceId));
        trace.setUpdated(LocalDateTime.now());
        save(trace);
        return trace.toRepresentation();
    }


    private Trace update(Trace trace, @Valid TraceUpdateRequest updateRequest) {
        trace.setHeaders(updateRequest.getHeaders());
        return trace;
    }


    private Trace patch(Trace trace, @Valid TraceUpdateRequest updateRequest) {
        updateRequest.getHeaders().forEach((key, value) -> {
            if (StringUtils.isBlank(value))
                trace.removeHeader(key);
            else
                trace.putHeader(key, value);
        });
        return trace;
    }


    @Override
    @Transactional
    public void createEvent(long traceId, @Valid EventCreationRequest creationRequest) throws ResourceNotFoundException {
        Trace trace = trace(traceId);
        Event event = Event.fromCreationRequest(creationRequest);
        trace.addEvent(event);
        save(trace);
    }


    @Override
    @Transactional(readOnly = true)
    public Collection<EventRepresentation> listEvents(long traceId) throws ResourceNotFoundException {
        Trace trace = trace(traceId);
        return trace.getEvents().stream().map(Event::toRepresentation).collect(Collectors.toList());
    }


    private Trace trace(long traceId) throws ResourceNotFoundException {
        return repo.findById(traceId).orElseThrow(() -> ResourceNotFoundException.trace(traceId));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<String> headerByName(long traceId, String headerName) throws ResourceNotFoundException {
        return Optional.ofNullable(trace(traceId).getHeaderMap().get(headerName));
    }


    @Override
    @Transactional
    public boolean putHeader(long traceId, @Valid HeaderCreationUpdateRequest header) throws ResourceNotFoundException {
        Trace trace = trace(traceId);
        boolean created = trace.putHeader(header.getName(), header.getValue());
        save(trace);
        return created;
    }


    @Override
    @Transactional
    public boolean deleteHeader(long traceId, String headerName) throws ResourceNotFoundException {
        Trace trace = trace(traceId);
        boolean removed = trace.removeHeader(headerName);
        save(trace);
        return removed;
    }


    private Trace save(Trace trace) {
        trace.setUpdated(LocalDateTime.now());
        repo.save(trace);
        return trace;
    }

}
