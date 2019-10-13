package com.kyripay.payment.infrastructure.adapter.out.trace;

import com.kyripay.payment.infrastructure.adapter.out.trace.dto.TraceCreationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "Traces")
public interface TracesClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/traces", consumes = "application/json")
    void createTrace(@RequestBody TraceCreationRequest request);

}
