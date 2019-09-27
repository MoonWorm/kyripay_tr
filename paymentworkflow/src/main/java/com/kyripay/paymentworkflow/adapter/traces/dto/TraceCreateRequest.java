package com.kyripay.paymentworkflow.adapter.traces.dto;

import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import lombok.Value;

import java.util.Map;

@Value
public class TraceCreateRequest {
    private Map<String, String> headers;
    private Event event;
}
