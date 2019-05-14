package com.kyriba.kyripay.users.dao;

import com.kyriba.kyripay.users.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UsersRepository extends CrudRepository<User, UUID> {
}
