package com.kyripay.users.api;

import com.kyripay.users.dto.UserDetails;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.service.UserDetailsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserDetailsApi {

    private UserDetailsService userDetailsService;

    public UserDetailsApi(UserDetailsService userDetailsServicer) {
        this.userDetailsService = userDetailsServicer;
    }

    @ApiOperation("Get user's details")
    @GetMapping("/users/{userId}/details")
    UserDetails getUserDetails(@PathVariable String userId) throws UserNotFoundException {
        return userDetailsService.getUserDetails(UUID.fromString(userId));
    }

    @ApiOperation("Update details for the user")
    @PutMapping("/users/{userId}/details")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUserDetails(@PathVariable String userId,
                          @RequestBody UserDetails userDetails) throws UserNotFoundException {
        userDetailsService.updateUserDetails(UUID.fromString(userId), userDetails);
    }
}
