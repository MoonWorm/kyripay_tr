package com.kyriba.kyripay.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipient {
    private String firstName;
    private String lastName;
    private String bankName;
    private String bankAdress;
    private String accountNumber;
}
