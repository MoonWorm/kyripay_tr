package com.kyripay.acknowledgement.api.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ack {
    private String format;
    private String customer;
    private String data;
}
