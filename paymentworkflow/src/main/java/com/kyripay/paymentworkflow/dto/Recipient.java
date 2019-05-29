package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Recipient {
    @ApiModelProperty(value = "Recipient unique id (UUID)", example = "87acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID id;
    @ApiModelProperty(value = "First name", example = "Dmitry")
    private String firstName;
    @ApiModelProperty(value = "Last name", example = "Poplavsky")
    private String lastName;
    @ApiModelProperty(value = "Bank name", example = "Prior bank")
    private String bankName;
    @ApiModelProperty(value = "Bank address", example = "Minsk")
    private String bankAddress;
    @ApiModelProperty(value = "Account number", example = "12345")
    private String accountNumber;
}
