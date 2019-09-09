package com.kyripay.payment.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@RequiredArgsConstructor
public class PaymentRecipientInfo
{

    @NotBlank(message = "First name must be specified")
    private final String firstName;
    @NotBlank(message = "Last name must be specified")
    private final String lastName;
    @NotBlank(message = "Recipient bank name must be specified")
    private final String bankName;
    @NotBlank(message = "Recipient bank address must be specified")
    private final String bankAddress;
    @NotBlank(message = "Recipient account number must be specified")
    private final String accountNumber;

}
