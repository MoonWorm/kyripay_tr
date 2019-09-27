package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;

import java.util.Map;
import java.util.Optional;

public interface Traces {
    Optional<Trace> getTraceById(long id);
    Trace createTrace(Map<String, String> headers, Event event);
    void updateHeaders(long traceId, Map<String, String> headers);
    void addEvent(long traceId, Event event);
}
