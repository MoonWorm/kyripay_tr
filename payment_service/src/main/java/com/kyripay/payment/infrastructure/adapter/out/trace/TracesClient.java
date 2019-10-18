package com.kyripay.payment.infrastructure.adapter.out.trace;

import com.kyripay.payment.infrastructure.adapter.out.trace.dto.TraceCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Traces")
public interface TracesClient {

    @PostMapping(value = "/api/v1/traces", consumes = "application/json")
    void createTrace(@RequestBody TraceCreationRequest request);

}
