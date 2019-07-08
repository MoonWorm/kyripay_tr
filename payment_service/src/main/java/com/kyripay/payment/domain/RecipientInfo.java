package com.kyripay.payment.domain;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class RecipientInfo
{

    @NotBlank(message = "First name must be specified")
    private String firstName;
    @NotBlank(message = "Last name must be specified")
    private String lastName;
    @NotBlank(message = "Recipient bank name must be specified")
    private String bankName;
    @NotBlank(message = "Recipient bank address must be specified")
    private String bankAddress;
    @NotBlank(message = "Recipient account number must be specified")
    private String accountNumber;

}
