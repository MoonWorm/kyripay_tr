package com.kyripay.paymentworkflow.domain.dto;

import lombok.Value;

@Value
public class PaymentToSendRequest {
    private Long paymentId;
    private String payloadUrl;
    private ConnectionLine connectionLine;
}
