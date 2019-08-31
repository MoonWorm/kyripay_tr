package com.kyripay.paymentworkflow.adapter;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import com.kyripay.paymentworkflow.domain.service.port.out.TraceAdapter;

import java.util.Map;

public class TraceAdapterImpl implements TraceAdapter {
    @Override
    public Trace createTrace(Map<String, Object> headers, Event event) {
        return null;
    }
}
