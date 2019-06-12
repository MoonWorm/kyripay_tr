package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentTransfer {
    @ApiModelProperty(value = "Unique paymentTransfer id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba46")
    private UUID id;
    @NotNull(message = "Payment must be provided")
    @ApiModelProperty(value = "Payment that should be send to the bank")
    private Payment payment;
}