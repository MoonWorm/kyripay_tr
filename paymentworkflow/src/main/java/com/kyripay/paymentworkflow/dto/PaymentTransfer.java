package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentTransfer {
    @ApiModelProperty(value = "Unique paymentTransfer id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba46")
    private UUID id;
    @ApiModelProperty(value = "Payment sender account", dataType = "com.kyripay.paymentworkflow.dto.Account")
    private Account account;
    @ApiModelProperty(value = "The list of transactions", dataType = "[Lcom.kyripay.paymentworkflow.dto.Transaction")
    private List<Transaction> transactions;
}