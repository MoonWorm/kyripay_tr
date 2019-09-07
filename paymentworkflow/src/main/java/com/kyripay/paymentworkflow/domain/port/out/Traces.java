package com.kyripay.paymentworkflow.domain.port.out;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;

import java.util.Map;
import java.util.Optional;

public interface Traces {
    Optional<Trace> getTraceById(long id);
    Trace createTrace(Map<String, Object> headers, Event event);
    void updateHeaders(Map<String, String> headers);
    void add(Event event);
}
