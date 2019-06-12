package com.kyripay.paymentworkflow.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Recipient {
    @NotBlank(message = "Recipient id can't be empty")
    @ApiModelProperty(value = "Recipient unique id (UUID)", example = "87acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID id;
    @NotBlank(message = "First name can't be empty")
    @ApiModelProperty(value = "First name", example = "Dmitry")
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
    @ApiModelProperty(value = "Last name", example = "Poplavsky")
    private String lastName;
    @NotBlank(message = "Bank name can't be empty")
    @ApiModelProperty(value = "Bank name", example = "Prior bank")
    private String bankName;
    @NotBlank(message = "Bank address can't be empty")
    @ApiModelProperty(value = "Bank address", example = "Minsk")
    private String bankAddress;
    @NotBlank(message = "Bank URN can't be empty")
    @ApiModelProperty(value = "Bank URN", example = "0000/00222/0XXXX")
    private String bankUrn;
    @NotBlank(message = "Account number can't be empty")
    @ApiModelProperty(value = "Account number", example = "12345")
    private String accountNumber;
}
