package com.kyripay.users.repository;

import com.kyripay.users.dto.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<Account, UUID> {
    Account getByUserIdAndId(UUID userId, UUID id);
    List<Account> getAllByUserId(UUID userId);
}