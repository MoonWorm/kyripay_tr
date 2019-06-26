package com.kyripay.users.api;

import com.kyripay.users.dto.Recipient;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.service.RecipientsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RecipientsApi {

    private RecipientsService recipientsService;

    public RecipientsApi(RecipientsService recipientsService) {
        this.recipientsService = recipientsService;
    }

    @ApiOperation("Create a recipient for a user")
    @PostMapping("/users/{userId}/recipients")
    @ResponseStatus(HttpStatus.CREATED)
    Recipient createUserRecipient(@PathVariable String userId,
                                  @RequestBody Recipient recipient) throws UserNotFoundException {
        return recipientsService.createRecipient(UUID.fromString(userId), recipient);
    }

    @ApiOperation("Get a recipient for a user")
    @GetMapping("/users/{userId}/recipients/{recipientId}")
    Recipient getUserRecipient(@PathVariable String userId,
                               @PathVariable String recipientId) throws UserNotFoundException {
        Recipient recipient = recipientsService.getRecipient(UUID.fromString(userId), UUID.fromString(recipientId));
        if (recipient == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Recipient with id %ss not found", recipientId));
        return recipient;
    }

    @ApiOperation("Get all user's recipients")
    @GetMapping("/users/{userId}/recipients")
    List<Recipient> getUserRecipients(@PathVariable String userId) throws UserNotFoundException {
        return recipientsService.getAllRecipients(UUID.fromString(userId));
    }

    @ApiOperation("Update a recipient")
    @PutMapping("/users/{userId}/recipients/{recipientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateRecipient(@PathVariable String userId,
                          @PathVariable String recipientId,
                          @RequestBody Recipient recipient) throws UserNotFoundException {
        if (recipientId.equals(recipient.getId().toString())) {
            recipientsService.updateRecipient(UUID.fromString(userId), recipient);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Different recipient id's");
        }
    }

    @ApiOperation("Delete a recipient for a user")
    @DeleteMapping("/users/{userId}/recipients/{recipientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserRecipient(@PathVariable String userId,
                             @PathVariable String recipientId) throws UserNotFoundException {
        recipientsService.deleteRecipient(UUID.fromString(userId), UUID.fromString(recipientId));
    }
}
