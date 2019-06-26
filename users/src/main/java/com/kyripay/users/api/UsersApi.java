package com.kyripay.users.api;


import com.kyripay.users.dto.User;
import com.kyripay.users.service.UsersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class UsersApi
{
    private UsersService usersService;

    public UsersApi(UsersService usersService) {
        this.usersService = usersService;
    }

    @ApiOperation("Create a user")
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    User createUser(@RequestBody User user) {
        return usersService.createUser(user);
    }

    @ApiOperation("Get a user")
    @GetMapping("/users/{id}")
    User getUser(@PathVariable String id) {
        return usersService.getUser(UUID.fromString(id));
    }

    @ApiOperation("Get all users")
    @GetMapping("/users")
    List<User> getUsers() {
        return usersService.getAllUsers();
    }

    @ApiOperation("Update a user")
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable String id) {
        usersService.deleteUser(UUID.fromString(id));
    }
}