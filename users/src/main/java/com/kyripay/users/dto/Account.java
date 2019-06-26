package com.kyripay.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "accounts")
public class Account {
    @ApiModelProperty(value = "Account unique id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    @Id
    @GeneratedValue
    private UUID id;
    @ApiModelProperty(value = "User id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
    @Column(name = "user_id")
    @JsonIgnore
    private UUID userId;
    @NotNull(message = "Bank id must be provided")
    @ApiModelProperty(value = "Bank id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba44")
    private UUID bankId;
    @NotBlank(message = "Account number can't be empty")
    @ApiModelProperty(value = "Number", example = "12345")
    private String number;
    @NotBlank(message = "Account Currency can't be empty")
    @ApiModelProperty(value = "Currency", example = "EUR")
    private String currency;
}
