package com.jorji.chat.userresolver.services;


import com.jorji.chat.userresolver.repositories.ResolutionUserRepository;
import com.jorji.chatutil.userutil.model.ResolverUserModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResolverService {
    private final ResolutionUserRepository repository;

    public ResolverUserModel getUserByUUID(UUID uuid) throws NoSuchElementException {
        ResolverUserModel user = repository.getResolverUserModelByUuid(uuid);
        if (user == null) throw new NoSuchElementException();
        return user;
    }

    /*
    public UUID getUUIDofUser(String username) throws NoSuchElementException, PrivateUserException {
        FullUserModel user = repository.getUserByUsername(username);
        if (user == null) throw new NoSuchElementException(username + " does not exists!");
        if (user.isPrivate()) throw new PrivateUserException(username + " does not wish to be found!");
        return user.getUuid();
    }

    public UUID getUUIDofContact(String contactId) throws NoSuchElementException {
        FullUserModel user = repository.getUserByContactId(contactId);
        if (user == null) throw new NoSuchElementException(contactId + " does not exists!");
        return user.getUuid();
    }

    */
}
