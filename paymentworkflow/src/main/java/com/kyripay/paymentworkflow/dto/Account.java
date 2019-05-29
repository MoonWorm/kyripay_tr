package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Account {
    @ApiModelProperty(value = "Account unique id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID id;
    @ApiModelProperty(value = "Bank id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID bankId;
    @ApiModelProperty(value = "Number", example = "12345")
    private String number;
    @ApiModelProperty(value = "Currency", example = "EUR")
    private String currency;
}
