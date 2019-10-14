package com.kyripay.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTemplateRecipientInfo {
    private String firstName;
    private String lastName;
    private String bankUrn;
    private String bankName;
    private String bankAddress;
    private String accountNumber;
}
