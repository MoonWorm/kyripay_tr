package com.kyripay.payment.domain;

import com.kyripay.payment.domain.vo.Amount;
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
public class PaymentTemplate {

    private Long id;
    @NotNull(message = "Payment template amount must be specified")
    private Amount amount;
    @NotNull(message = "Customer bank must be specified")
    private Long bankId;
    @NotBlank(message = "Account number must be specified")
    private String accountNumber;
    @NotBlank(message = "Payment template name must be specified")
    private String name;
    @Valid
    @NotNull(message = "Recipient info must be specified")
    private RecipientInfo recipientInfo = new RecipientInfo();
    private LocalDateTime createdOn;

}
