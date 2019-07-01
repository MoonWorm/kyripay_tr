package com.kyripay.acknowledgement.dto;

public enum AckLevel {
    ERROR            (0),
    SENT_TO_BANK     (1),
    ACKED_BY_NETWORK (2),
    RECEIVED_BY_BANK(3),
    EXECUTED_BY_BANK (4);

    final int code;

    AckLevel(int code) { this.code = code; }

    public static AckLevel fromCode(int code) {
        for (AckLevel s: values()) {
            if (s.code == code) {
                return s;
            }
        }
        throw new IllegalArgumentException(String.valueOf(code));
    }

    public int code(){
        return code;
    }

}
