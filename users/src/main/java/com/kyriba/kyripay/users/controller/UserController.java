package com.kyriba.kyripay.users.controller;


import com.kyriba.kyripay.users.model.User;
import com.kyriba.kyripay.users.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
public class UserController
{
    private final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/v1/users")
    User convert(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/v1/users")
    List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/v1/users/{id}")
    Optional<User> getUser(@PathVariable String id){
        return userService.getUser(UUID.fromString(id));
    }
}
