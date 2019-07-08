package com.kyripay.payment.domain;

import com.kyripay.payment.domain.vo.Amount;
import com.kyripay.payment.domain.vo.Status;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Payment {

    private Long id;
    @NotNull(message = "Payment amount must be specified")
    private Amount amount;
    @NotNull(message = "Customer bank must be specified")
    private Long bankId;
    @NotBlank(message = "Account number must be specified")
    private String accountNumber;
    @NotNull(message = "Payment status must be specified")
    private Status status;
    @Valid
    @NotNull(message = "Recipient info must be specified")
    private RecipientInfo recipientInfo = new RecipientInfo();
    private LocalDateTime createdOn;

}