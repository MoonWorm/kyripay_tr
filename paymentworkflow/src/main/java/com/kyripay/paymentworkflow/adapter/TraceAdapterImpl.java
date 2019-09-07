package com.kyripay.paymentworkflow.adapter;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import com.kyripay.paymentworkflow.domain.port.out.Traces;

import java.util.Map;
import java.util.Optional;

public class TraceAdapterImpl implements Traces {

    @Override
    public Optional<Trace> getTraceById(long id) {
        return Optional.empty();
    }

    @Override
    public Trace createTrace(Map<String, Object> headers, Event event) {
        return null;
    }

    @Override
    public void updateHeaders(Map<String, String> headers) {

    }

    @Override
    public void add(Event event) {

    }
}
