package com.kyripay.paymentworkflow.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversionResult {

    enum Status { PROCESSING, READY, CONVERSION_FAILED }

    private Long paymentId;
    private Status status;
    private String payloadUrl;
}
