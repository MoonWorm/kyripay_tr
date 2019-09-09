package com.kyripay.payment.domain;

import com.kyripay.payment.domain.vo.Amount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PaymentTemplate {

    private Long id;
    @NotBlank(message = "Payment template name must be specified")
    private final String name;
    private Amount amount;
    private Long bankId;
    private String accountNumber;
    private PaymentTemplateRecipientInfo recipientInfo = new PaymentTemplateRecipientInfo();
    private LocalDateTime createdOn;

}
