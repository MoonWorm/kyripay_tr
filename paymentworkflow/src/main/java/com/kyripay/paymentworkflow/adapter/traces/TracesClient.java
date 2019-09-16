package com.kyripay.paymentworkflow.adapter.traces;

import com.kyripay.paymentworkflow.adapter.traces.dto.TraceCreateRequest;
import com.kyripay.paymentworkflow.domain.dto.trace.Event;
import com.kyripay.paymentworkflow.domain.dto.trace.Trace;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Optional;

@FeignClient("traces")
public interface TracesClient {
    @RequestMapping(method = RequestMethod.GET, value = "api/v1/traces/{traceId}", consumes = "application/json")
    public Optional<Trace> getTraceById(@PathVariable("traceId") long id);

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/traces", consumes = "application/json")
    public Trace createTrace(TraceCreateRequest traceCreateRequest);

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/traces/{traceId}/headers", consumes = "application/json")
    public void updateHeaders(@PathVariable("traceId")long id, Map<String, String> headers);

    @RequestMapping(method = RequestMethod.POST, value = "api/v1/traces/{traceId}/events", consumes = "application/json")
    public void add(@PathVariable("traceId")long id, Event event);
}
