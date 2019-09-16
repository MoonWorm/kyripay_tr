package com.kyripay.paymentworkflow.adapter.traces;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import com.kyripay.paymentworkflow.domain.port.out.Traces;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class TracesImpl implements Traces {
    @Override
    public Optional<Trace> getTraceById(long id) {
        return Optional.empty();
    }

    @Override
    public Trace createTrace(Map<String, String> headers, Event event) {
        return null;
    }

    @Override
    public void updateHeaders(Map<String, String> headers) {

    }

    @Override
    public void add(Event event) {

    }
}
