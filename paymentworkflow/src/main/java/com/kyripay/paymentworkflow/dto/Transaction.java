package com.kyripay.paymentworkflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
    String currency;
    Float amount;
    Recipient recipient;
}

