package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.model.UserIdentifiers;
import com.jorji.chat.routingservice.repositories.UserIdentifiersRepository;
import com.jorji.chatutil.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserResolverService {
    private final UserIdentifiersRepository repository;

    public ChatMessage resolveDestUsername(ChatMessage message){
        UserIdentifiers uid = repository.findUserIdentifiersByUsername(message.getDestination())
                .orElseThrow(NoSuchElementException::new);
        message.setDestination(uid.getUuid().toString());
        return message;
    }

    public ChatMessage resolveDestContactId(ChatMessage message){
        UserIdentifiers uid = repository.findUserIdentifiersByContactId(message.getDestination())
                .orElseThrow(NoSuchElementException::new);;
        message.setDestination(uid.getUuid().toString());
        return message;
    }
}
