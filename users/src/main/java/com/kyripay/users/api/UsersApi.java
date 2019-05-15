package com.kyripay.users.api;


import com.kyripay.users.dto.Account;
import com.kyripay.users.dto.Recipient;
import com.kyripay.users.dto.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kyripay.users.api.Dummies.*;


@RestController
public class UsersApi
{
    @ApiOperation("Get all users")
    @GetMapping("/v1/users")
    List<User> getUsers()
    {
        List<User> users = new ArrayList<>();
        users.add(getDummyUser());
        return users;
    }

    @ApiOperation("Get a user")
    @GetMapping("/v1/users/{userId}")
    User getUser(@PathVariable String id) {
        User user = getDummyUser();
        user.setId(UUID.fromString(id));
        return user;
    }

    @ApiOperation("Create a user")
    @PostMapping("/v1/users")
    User createUser(@RequestBody User user) {
        user.setId(UUID.randomUUID());
        return user;
    }

    @ApiOperation("Activate a user")
    @PostMapping("/v1/users/{userId}/activation")
    @ResponseStatus(code = HttpStatus.CREATED)
    void activateUser(@PathVariable String userId) { }

    @ApiOperation("Deactivate a user")
    @PostMapping("/v1/users/{userId}/deactivation")
    @ResponseStatus(code = HttpStatus.CREATED)
    void deativateUser(@PathVariable String userId) { }

    @ApiOperation("Activate a user")
    @PatchMapping("/v1/users/{userId}")
    User updateUser(@PathVariable String userId, @RequestBody User user) {
        return getDummyUser();
    }


    //=====  accounts
    @ApiOperation("Get accounts list for a user")
    @GetMapping("/v1/users/{id}/accounts")
    List<Account> getUserAccounts(@PathVariable String id) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(getDummyAccount());
        return accounts;
    }

    @ApiOperation("Get user's account")
    @GetMapping("v1/users/{userId}/accounts/{accountId}")
    Optional<Account> getAccount(@PathVariable String userId, @PathVariable String accountId) {
        return Optional.of(getDummyAccount());
    }

    @ApiOperation("Create new account for a user")
    @PostMapping("/v1/users/{userId}/accounts")
    Account createAccount(@PathVariable String userId, @RequestBody Account account) {
        return getDummyAccount();
    }

    @ApiOperation("Update an account for the user")
    @PutMapping("/v1/users/{userId}/accounts/{accountId}")
    Account updateAccount(@PathVariable String userId, @PathVariable String accountId) {
        return getDummyAccount();
    }

    @ApiOperation("Delete an account from user")
    @DeleteMapping("v1/users/{usrId}/accounts/{accountId}")
    void deleteAccount(@PathVariable String userId, @PathVariable String accountId) {    }


    //==== Recipients
    // maybe move Recipients to separate Data Rest Repo with the link to User as userId?
    @ApiOperation("Get all user's recipients")
    @GetMapping("/v1/users/{id}/recipients")
    List<Recipient> getUserRecipients(@PathVariable String id) {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(getDummyRecipient());
        return recipients;
    }

    @ApiOperation("Get a recipient for a user")
    @GetMapping("/v1/users/{userId}/recipients/{recipientId}")
    Optional<Recipient> getUserRecipient(@PathVariable String id, @PathVariable String recipientId) {
        return Optional.of(getDummyRecipient());
    }

    @ApiOperation("Create a recipient for a user")
    @PostMapping("/v1/users/{id}/recipients")
    Recipient createUserRecipient(@PathVariable String id, @RequestBody Recipient recipient) {
        return getDummyRecipient();
    }

    @ApiOperation("Update a recipient")
    @PutMapping("/v1/users/{userId}/recipients/{recipientId}")
    Recipient updateRecipient(@PathVariable String id, @RequestBody Recipient recipient) {
        return recipient;
    }

    @ApiOperation("Delete a recipient for a user")
    @DeleteMapping("/v1/users/{userId}/recipients/{recipientId}")
    void deleteUserRecipient(@PathVariable String userId, @PathVariable String recipientId) {    }
}
