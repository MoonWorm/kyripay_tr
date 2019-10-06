package com.kyripay.connectivity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum Protocol {
    PROTOCOL_1("Protocol 1", 0, "Test protocol #1"),
    PROTOCOL_2("Protocol 2", 1, "Test protocol #2");

    private String name;
    private int code;
    private String description;
}
