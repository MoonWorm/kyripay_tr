package com.kyripay.users.service;

import com.kyripay.users.dto.UserDetails;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.repository.UserDetailsRepository;
import com.kyripay.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.UUID;

@Service
@Transactional
@Validated
public class UserDetailsService {

    private UserDetailsRepository userDetailsRepository;
    private UsersRepository usersRepository;

    public UserDetailsService(UserDetailsRepository userDetailsRepository, UsersRepository usersRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.usersRepository = usersRepository;
    }

    public UserDetails getUserDetails(UUID userId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        return userDetailsRepository.getByUser_Id(userId);
    }

    public void updateUserDetails(UUID userId, @Valid UserDetails updatedDetails) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        UserDetails userDetails = getUserDetails(userId);
        userDetails.setActive(updatedDetails.isActive());
        userDetails.setAddress(updatedDetails.getAddress());
        userDetails.setEmail(updatedDetails.getEmail());
        userDetails.setFirstName(updatedDetails.getFirstName());
        userDetails.setLastName(updatedDetails.getLastName());
        userDetails.setUserGroup(updatedDetails.getUserGroup());
        userDetails.setPhoneNumber(updatedDetails.getPhoneNumber());
        userDetails.setSecretHash(updatedDetails.getSecretHash());
        userDetailsRepository.save(userDetails);
    }
}
