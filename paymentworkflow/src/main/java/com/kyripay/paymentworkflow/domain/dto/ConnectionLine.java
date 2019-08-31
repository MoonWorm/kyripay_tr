package com.kyripay.paymentworkflow.domain.dto;

import lombok.Value;

@Value
public class ConnectionLine {

    static class ConnectionInfo {
        String Url;
        String login;
        String password;
    }

    private String protocol;
    private String name;
    private ConnectionInfo connectionInfo;
}
