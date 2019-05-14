package com.kyriba.kyripay.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    private String number;
    private String currency;
}
