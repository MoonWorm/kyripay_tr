package com.kyripay.acknowledgement.dto;

public enum AckStatus {
    REJECTED        (0),
    UNCHANGED       (1),
    TRANSMITTED     (2),
    ACK_RECEIVED    (3),
    PARTIALLY_VALID (4),
    VALID           (5);

    final int code;

    AckStatus(int code) { this.code = code; }

    public int getCode() {
        return this.code;
    }

    public static AckStatus fromCode(int code) {
        for (AckStatus s: values()) {
            if (s.code == code) {
                return s;
            }
        }
        throw new IllegalArgumentException(String.valueOf(code));
    }
}
