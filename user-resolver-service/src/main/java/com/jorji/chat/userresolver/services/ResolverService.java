package com.jorji.chat.userresolver.services;


import com.jorji.chat.userresolver.repositories.ResolutionUserRepository;
import com.jorji.chatutil.userutil.model.SlimUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResolverService {
    private final ResolutionUserRepository repository;

    public SlimUser getUserByUUID(UUID uuid) throws NoSuchElementException {
        SlimUser user = repository.getSlimUserByUuid(uuid);
        if (user == null) throw new NoSuchElementException();
        return user;
    }
}
