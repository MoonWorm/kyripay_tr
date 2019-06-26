package com.kyripay.users.api;

import com.kyripay.users.dto.Account;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.service.AccountsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AccountsApi {

    private AccountsService accountsService;

    public AccountsApi(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @ApiOperation("Create new account for a user")
    @PostMapping("/users/{userId}/accounts")
    @ResponseStatus(HttpStatus.CREATED)
    Account createAccount(@PathVariable String userId,
                          @RequestBody Account account) throws UserNotFoundException {
        return accountsService.createAccount(UUID.fromString(userId), account);
    }

    @ApiOperation("Get user's account")
    @GetMapping("/users/{userId}/accounts/{accountId}")
    Account getAccount(@PathVariable String userId,
                       @PathVariable String accountId) throws UserNotFoundException {
        Account account = accountsService.getAccount(UUID.fromString(userId), UUID.fromString(accountId));
        if (account == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("account with id %s not fount", accountId));
        return account;
    }

    @ApiOperation("Get accounts list for a user")
    @GetMapping("/users/{userId}/accounts")
    List<Account> getUserAccounts(@PathVariable String userId) throws UserNotFoundException {
        return accountsService.getAllAccounts(UUID.fromString(userId));
    }

    @ApiOperation("Update an account for the user")
    @PutMapping("/users/{userId}/accounts/{accountId}")
    Account updateAccount(@PathVariable String userId,
                          @PathVariable String accountId,
                          @RequestBody Account newAccount) throws UserNotFoundException {
        if (accountId.equals(newAccount.getId().toString())) {
            return accountsService.updateAccount(UUID.fromString(userId), newAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Different account id's");
        }
    }

    @ApiOperation("Delete an account from user")
    @DeleteMapping("/users/{userId}/accounts/{accountId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteAccount(@PathVariable String userId,
                       @PathVariable String accountId) throws UserNotFoundException {
        accountsService.deleteAccount(UUID.fromString(userId), UUID.fromString(accountId));
    }
}
