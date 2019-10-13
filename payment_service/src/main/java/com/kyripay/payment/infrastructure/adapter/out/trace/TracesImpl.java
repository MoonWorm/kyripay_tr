package com.kyripay.payment.infrastructure.adapter.out.trace;

import com.kyripay.payment.infrastructure.adapter.out.trace.dto.TraceCreationRequest;
import com.kyripay.payment.domain.Trace;
import com.kyripay.payment.domain.port.out.traces.Traces;
import org.springframework.stereotype.Service;

@Service
public class TracesImpl implements Traces {

    private final TracesClient tracesClient;

    public TracesImpl(TracesClient tracesClient) {
        this.tracesClient = tracesClient;
    }

    @Override
    public void createTrace(Trace trace) {
        TraceCreationRequest request = new TraceCreationRequest(trace.getPaymentId());
        request.setHeaders(trace.getHeaders());

        tracesClient.createTrace(request);
    }

}
