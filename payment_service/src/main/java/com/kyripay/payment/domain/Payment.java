package com.kyripay.payment.domain;

import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Payment {

    private Long id;
    @NotNull(message = "User id must be specified")
    private UUID userId;
    @NotNull(message = "Payment amount must be specified")
    private final Amount amount;
    @NotNull(message = "Customer bank must be specified")
    private final Long bankId;
    @NotBlank(message = "Account number must be specified")
    private final String accountNumber;
    @NotNull(message = "Payment status must be specified")
    private final Status status;
    @Valid
    @NotNull(message = "Recipient info must be specified")
    private final PaymentRecipientInfo recipientInfo;
    private final LocalDateTime createdOn;

}