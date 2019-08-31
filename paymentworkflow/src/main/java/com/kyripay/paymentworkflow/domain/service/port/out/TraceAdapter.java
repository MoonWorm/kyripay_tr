package com.kyripay.paymentworkflow.domain.service.port.out;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;

import java.util.Map;

public interface TraceAdapter {
    public Trace createTrace(Map<String, Object> headers, Event event);
}
