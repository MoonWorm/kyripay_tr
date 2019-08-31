package com.kyripay.paymentworkflow.domain.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Payment {
    @ApiModelProperty(value = "Unique identifier", example = "4")
    private long id;

    @ApiModelProperty(value = "Unique user id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
    private UUID userId;

    @ApiModelProperty(value = "Payment status", example = "COMPLETED")
    private Status status;

    private PaymentDetails paymentDetails;

    @ApiModelProperty(value = "Payment creation time in millis since Epoch in UTC zone", example = "12343252134")
    private long createdOn;

    public enum Status {
        CREATED, PROCESSING, COMPLETED
    }
}
