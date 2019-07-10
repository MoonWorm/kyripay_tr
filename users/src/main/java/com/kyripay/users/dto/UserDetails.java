package com.kyripay.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue
    @JsonIgnore
    @Type(type = "pg-uuid")
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
    @ApiModelProperty(value = "Email", example = "dmitry@gmail.com")
    private String email;
    @NotBlank(message = "Phone number can't be empty")
    @ApiModelProperty(value = "Phone number", example = "7788")
    private String phoneNumber;
    @NotBlank(message = "Password can't be empty")
    @ApiModelProperty(value = "Password hash", example = "5F4DCC3B5AA765D61D8327DEB882CF99")
    private String secretHash;
    @NotNull(message = "Group must be provided")
    @ApiModelProperty(value = "Group", example = "CUSTOMER")
    @Enumerated(EnumType.STRING)
    private UserDetails.Group userGroup;
    @NotNull(message = "User status 'isActive' must be provided")
    @ApiModelProperty(value = "Is user active", example = "true")
    private boolean active;

    @JsonIgnore
    @ApiModelProperty(value = "User id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
    @OneToOne(mappedBy = "userDetails", fetch = FetchType.LAZY)
    private User user;


    public enum Group {
        CUSTOMER,
        PRODUCT
    }
}
