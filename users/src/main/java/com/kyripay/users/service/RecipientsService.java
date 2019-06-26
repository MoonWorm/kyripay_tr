package com.kyripay.users.service;

import com.kyripay.users.dto.Recipient;
import com.kyripay.users.exceptions.UserNotFoundException;
import com.kyripay.users.repository.RecipientsRepository;
import com.kyripay.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Validated
public class RecipientsService {
    private RecipientsRepository recipientsRepository;
    private UsersRepository usersRepository;

    public RecipientsService(RecipientsRepository recipientsRepository, UsersRepository usersRepository) {
        this.recipientsRepository = recipientsRepository;
        this.usersRepository = usersRepository;
    }

    public Recipient createRecipient(UUID userId, @Valid Recipient recipient) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        recipient.setUserId(userId);
        return recipientsRepository.save(recipient);
    }

    public Recipient getRecipient(UUID userId, UUID recipientId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        return recipientsRepository.getByUserIdAndId(userId, recipientId);
    }

    public List<Recipient> getAllRecipients(UUID userId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        return recipientsRepository.getAllByUserId(userId);
    }

    public void updateRecipient(UUID userId, @Valid Recipient updatedRecipient) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        Recipient recipient = getRecipient(userId, updatedRecipient.getId());
        recipient.setBankUrn(updatedRecipient.getBankUrn());
        recipient.setLastName(updatedRecipient.getLastName());
        recipient.setFirstName(updatedRecipient.getFirstName());
        recipient.setBankName(updatedRecipient.getBankName());
        recipient.setBankAddress(updatedRecipient.getBankAddress());
        recipient.setAccountNumber(updatedRecipient.getAccountNumber());
        recipientsRepository.save(recipient);
    }

    public void deleteRecipient(UUID userId, UUID recipientId) throws UserNotFoundException {
        if (!usersRepository.existsById(userId)) throw new UserNotFoundException();
        Recipient recipient = recipientsRepository.getByUserIdAndId(userId, recipientId);
        recipientsRepository.delete(recipient);
    }
}
