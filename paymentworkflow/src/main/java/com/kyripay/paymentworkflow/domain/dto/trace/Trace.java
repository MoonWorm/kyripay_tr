package com.kyripay.paymentworkflow.domain.dto.trace;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Trace {
    private final long paymentId;
    private final Map<String, String> headers;
    private final LocalDateTime created;
    private final LocalDateTime updated;
}
