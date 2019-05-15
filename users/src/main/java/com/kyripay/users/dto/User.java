package com.kyripay.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User {
    @ApiModelProperty(value = "Unique user id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba46")
    private UUID id;
    @ApiModelProperty(value = "First name", example = "Dmitry")
    private String firstName;
    @ApiModelProperty(value = "Last name", example = "Ivanov")
    private String lastName;
    @ApiModelProperty(value = "Address", example = "Kalvariuskaia 42, Minsk")
    private String adress;
    @ApiModelProperty(value = "Email", example = "kyriba@gmail.com")
    private String email;
    @ApiModelProperty(value = "Phone number", example = "7788")
    private String phoneNumber;
    @ApiModelProperty(value = "Password hash", example = "5F4DCC3B5AA765D61D8327DEB882CF99")
    private String passwordHash;
    @ApiModelProperty(value = "Group", example = "CUSTOMER")
    private Group group;
    @ApiModelProperty(value = "Is user active", example = "true")
    private boolean isActive;
    @ApiModelProperty(value = "The list of accounts", example = "[]")
    private List<Account> accounts;
    @ApiModelProperty(value = "The list of recipients", example = "[]")
    private List<Recipient> recipients;

    public enum Group {
        CUSTOMER,
        PRODUCT
    }
}
