package com.kyripay.paymentworkflow.adapter.traces;

import com.kyripay.paymentworkflow.adapter.traces.dto.TraceCreateRequest;
import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import com.kyripay.paymentworkflow.domain.port.out.Traces;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TracesImpl implements Traces {

    private final TracesClient tracesClient;

    public TracesImpl(TracesClient tracesClient) {
        this.tracesClient = tracesClient;
    }

    @Override
    public Optional<Trace> getTraceById(long id) {
        return tracesClient.getTraceById(id);
    }

    @Override
    public Trace createTrace(Map<String, String> headers, Event event) {
        TraceCreateRequest traceCreateRequest = new TraceCreateRequest(headers, event);
        return tracesClient.createTrace(traceCreateRequest);
    }

    @Override
    public void updateHeaders(long traceId, Map<String, String> headers) {
        tracesClient.updateHeaders(traceId, headers);
    }

    @Override
    public void addEvent(long traceId, Event event) {
        tracesClient.add(traceId, event);
    }
}
