package com.kyripay.paymentworkflow.domain.dto;

import lombok.Value;

import java.util.Map;

@Value
public class EmailNotificationRequest {
    private String titleTemplateId;
    private String to;
    private String bodyTemplateId;
    private Map<String, Object> parameters;
}
