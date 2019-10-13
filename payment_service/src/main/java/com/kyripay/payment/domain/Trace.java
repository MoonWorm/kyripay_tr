package com.kyripay.payment.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Trace {

    private final long paymentId;
    private Map<String, String> headers = new HashMap<>();

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}