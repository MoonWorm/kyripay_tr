package com.kyripay.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "recipients")
public class Recipient {
    @ApiModelProperty(value = "Recipient unique id (UUID)", example = "87acc585-dcf6-49ad-ae95-3422a5cdba46")
    @Id
    @GeneratedValue
    private UUID id;
    @ApiModelProperty(value = "User id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
    @Column(name = "user_id")
    @JsonIgnore
    private UUID userId;
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
