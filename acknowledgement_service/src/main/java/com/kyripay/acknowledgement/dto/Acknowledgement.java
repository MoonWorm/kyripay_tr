package com.kyripay.acknowledgement.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Acknowledgement {
    private String format;
    private String customer;
    private String data;
}
