package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentDetails {

    @Valid
    @NotNull(message = "Payment amount must be specified")
    private Amount amount;

    @NotNull(message = "Customer bank must be specified")
    @ApiModelProperty(value = "Bank name that will be used for the payment", example = "General Bank Inc.")
    private Long bankId;

    @NotBlank(message = "Account number must be specified")
    @ApiModelProperty(value = "Account number of the mentioned bank that will be used for the payment", example = "12345")
    private String accountNumber;

    @Valid
    @NotNull(message = "Recipient info must be specified")
    private RecipientInfo recipientInfo;

}

