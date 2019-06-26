package com.kyripay.users.service;

import com.kyripay.users.dto.Account;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.repository.AccountsRepository;
import com.kyripay.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Validated
public class AccountsService {

    private AccountsRepository accountsRepository;
    private UsersRepository usersRepository;

    public AccountsService(AccountsRepository accountsRepository, UsersRepository usersRepository) {
        this.accountsRepository = accountsRepository;
        this.usersRepository = usersRepository;
    }

    public Account createAccount(UUID userId, @Valid Account account) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        account.setUserId(userId);
        return accountsRepository.save(account);
    }

    public Account getAccount(UUID userId, UUID accountId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        return accountsRepository.getByUserIdAndId(userId, accountId);
    }

    public List<Account> getAllAccounts(UUID userId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        return accountsRepository.getAllByUserId(userId);
    }

    public Account updateAccount(UUID userId, @Valid Account updatedAccount) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        Account account = accountsRepository.getByUserIdAndId(userId, updatedAccount.getId());
        account.setBankId(updatedAccount.getBankId());
        account.setNumber(updatedAccount.getNumber());
        account.setCurrency(updatedAccount.getCurrency());
        return accountsRepository.save(account);
    }

    public void deleteAccount(UUID userId, UUID accountId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        Account account = accountsRepository.getByUserIdAndId(userId, accountId);
        accountsRepository.delete(account);
    }
}
