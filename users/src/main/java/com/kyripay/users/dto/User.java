package com.kyripay.users.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @ApiModelProperty(value = "Unique user id (UUID)", example = "88acc585-dcf6-49ad-ae95-3422a5cdba45")
    private UUID id;
    @NotNull(message = "User details should be provided")
    @Valid
    @ApiModelProperty(value = "User details")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true, targetEntity = UserDetails.class)
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;
    @ApiModelProperty(value = "The list of recipients")
    @Valid
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true, targetEntity = Recipient.class)
    @JoinColumn(name = "user_id")
    private Set<Recipient> recipients;
    @ApiModelProperty(value = "The list of accounts")
    @Valid
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true, targetEntity = Account.class)
    @JoinColumn(name = "user_id")
    private Set<Account> accounts;
}
