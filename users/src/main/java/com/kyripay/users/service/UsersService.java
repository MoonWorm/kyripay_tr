package com.kyripay.users.service;

import com.kyripay.users.dto.User;
import com.kyripay.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional
public class UsersService {

    private UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User createUser(@Valid User user) {
        return usersRepository.save(user);
    }

    public User getUser(UUID id) {
        return usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public void deleteUser(UUID id) {
        usersRepository.deleteById(id);
    }
}
