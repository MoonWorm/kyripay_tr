package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Payment {
    @NotBlank(message = "Payment id can't be empty")
    @ApiModelProperty(value = "Unique payment id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba46")
    private UUID id;
    @NotNull(message = "Account must be provided")
    @ApiModelProperty(value = "Payment sender account", dataType = "com.kyripay.paymentworkflow.dto.Account")
    private Account account;
    @NotNull(message = "Transactions must be provided")
    @ApiModelProperty(value = "The list of transactions", dataType = "[Lcom.kyripay.paymentworkflow.dto.Transaction")
    private List<Transaction> transactions;
}
