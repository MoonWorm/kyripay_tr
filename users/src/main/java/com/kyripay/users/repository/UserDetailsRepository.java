package com.kyripay.users.repository;

import com.kyripay.users.dto.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    UserDetails getByUser_Id(UUID userId);
}