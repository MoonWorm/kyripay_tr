package com.kyriba.kyripay.users.service;

import com.kyriba.kyripay.users.dao.UsersRepository;
import com.kyriba.kyripay.users.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    UsersRepository usersRepository;

    public User createUser(User user) {
        return usersRepository.save(user);
    }

    public Optional<User> getUser(UUID id) {
        return usersRepository.findById(id);
    }

    public List<User> getUsers() {
        ArrayList<User> result = new ArrayList<>();
        usersRepository.findAll().forEach(result::add);
        return result;
    }
}
