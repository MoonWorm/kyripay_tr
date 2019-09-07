package com.kyripay.paymentworkflow.domain.dto.trace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    public enum Type {SUCCESS, ERROR, INFO}

    private String name;
    private String source;
    private Type type;
    private String comment;
    private LocalDateTime created;
}