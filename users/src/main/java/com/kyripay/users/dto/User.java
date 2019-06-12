package com.kyripay.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {
    @ApiModelProperty(value = "Unique user id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba46")
    private UUID id;
    @NotBlank(message = "First name can't be empty")
    @ApiModelProperty(value = "First name", example = "Dmitry")
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
    @ApiModelProperty(value = "Last name", example = "Ivanov")
    private String lastName;
    @NotBlank(message = "Address can't be empty")
    @ApiModelProperty(value = "Address", example = "Kalvariuskaia 42, Minsk")
    private String address;
    @NotBlank(message = "Email can't be empty")
    @ApiModelProperty(value = "Email", example = "kyriba@gmail.com")
    private String email;
    @NotBlank(message = "Phone number can't be empty")
    @ApiModelProperty(value = "Phone number", example = "7788")
    private String phoneNumber;
    @NotBlank(message = "Password can't be empty")
    @ApiModelProperty(value = "Password hash", example = "5F4DCC3B5AA765D61D8327DEB882CF99")
    private String passwordHash;
    @NotNull(message = "Group must be provided")
    @ApiModelProperty(value = "Group", example = "CUSTOMER")
    private Group group;
    @NotNull(message = "User status 'isActive' be provided")
    @ApiModelProperty(value = "Is user active", example = "true")
    private boolean isActive;
    @NotNull(message = "The list of accounts must be provided")
    @ApiModelProperty(value = "The list of accounts", example = "[]")
    private List<Account> accounts;
    @NotNull(message = "The list of recipients must be provided")
    @ApiModelProperty(value = "The list of recipients", example = "[]")
    private List<Recipient> recipients;

    public enum Group {
        CUSTOMER,
        PRODUCT
    }
}
