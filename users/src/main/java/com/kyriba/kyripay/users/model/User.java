package com.kyriba.kyripay.users.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String firstName;
    private String lastName;
    private String adress;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private Group group;
    private List<Account> accounts;
    private List<Recipient> recipients;

    enum Group {
        CUSTOMER,
        PRODUCT
    }
}
