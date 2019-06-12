package com.kyripay.users.api;

import com.kyripay.users.dto.Account;
import com.kyripay.users.dto.Recipient;
import com.kyripay.users.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Dummies {

    static User getDummyUser() {
        Recipient recipient = getDummyRecipient();

        Account account = getDummyAccount();

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(recipient);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setAccounts(accounts);
        user.setRecipients(recipients);
        user.setAddress("Kalvariuskaia 42, Minsk");
        user.setEmail("kyriba@gmail.com");
        user.setFirstName("Kyriba");
        user.setLastName("Kyriba");
        user.setPhoneNumber("7788");
        user.setGroup(User.Group.CUSTOMER);
        user.setActive(true);
        user.setPasswordHash("blahbla");
        return user;
    }

    static Recipient getDummyRecipient() {
        Recipient recipient = new Recipient();
        recipient.setId(UUID.randomUUID());
        recipient.setAccountNumber("12345");
        recipient.setBankAddress("kiev");
        recipient.setBankName("prior bank");
        recipient.setBankUrn("0000/00222/0XXXX");
        recipient.setFirstName("Mikalai");
        recipient.setLastName("Ivanov");
        return recipient;
    }

    static Account getDummyAccount() {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setBankId(UUID.randomUUID());
        account.setCurrency("EUR");
        account.setNumber("11122");
        return account;
    }
}