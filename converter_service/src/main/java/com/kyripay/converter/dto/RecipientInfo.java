package com.kyripay.converter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipientInfo {
    @NotBlank(message = "First name must be specified")
    @ApiModelProperty(value = "Recipient first name", example = "Vasia")
    private String firstName;

    @NotBlank(message = "Last name must be specified")
    @ApiModelProperty(value = "Recipient last name", example = "Pupkin")
    private String lastName;

    @NotBlank(message = "Recipient bank name must be specified")
    @ApiModelProperty(value = "Recipient bank name", example = "Foo&Bar and Co Bank")
    private String bankName;

    @NotBlank(message = "Recipient bank address must be specified")
    @ApiModelProperty(value = "Recipient bank address", example = "Italy, Milan, Main str., 1-2")
    private String bankAddress;

    @NotBlank(message = "Recipient account number must be specified")
    @ApiModelProperty(value = "Recipient account number", example = "1234567")
    private String accountNumber;

}
