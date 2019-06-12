package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Account {
    @ApiModelProperty(value = "Account unique id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID id;
    @NotBlank(message = "Bank id can't be empty")
    @ApiModelProperty(value = "Bank id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID bankId;
    @NotBlank(message = "Number id can't be empty")
    @ApiModelProperty(value = "Number", example = "12345")
    private String number;
    @NotBlank(message = "Currency id can't be empty")
    @ApiModelProperty(value = "Currency", example = "EUR")
    private String currency;
}

