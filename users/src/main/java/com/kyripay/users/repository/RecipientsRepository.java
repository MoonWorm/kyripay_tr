package com.kyripay.users.repository;

import com.kyripay.users.dto.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipientsRepository extends JpaRepository<Recipient, UUID> {
    List<Recipient> getAllByUserId(UUID uuid);
    Recipient getByUserIdAndId(UUID userId, UUID id);
}