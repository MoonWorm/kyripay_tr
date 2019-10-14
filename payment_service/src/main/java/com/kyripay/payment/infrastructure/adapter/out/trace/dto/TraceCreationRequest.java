package com.kyripay.payment.infrastructure.adapter.out.trace.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class TraceCreationRequest {

    private final Long paymentId;

    private Map<String, String> headers;


}
