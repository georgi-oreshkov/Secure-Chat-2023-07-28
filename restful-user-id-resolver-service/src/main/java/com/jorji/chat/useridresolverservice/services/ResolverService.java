package com.jorji.chat.useridresolverservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorji.chat.useridresolverservice.model.User;
import com.jorji.chat.useridresolverservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ResolverService {
    private final UserRepository repository;

    public byte[] getSerializedUUIDofUser(String username) throws NoSuchElementException {
        User user = repository.getUserByUsername(username);
        if (user == null) throw new NoSuchElementException(username + " does not exists!");
        try {
            return serializeUUIDToMessagePack(user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getSerializedUUIDofContact(String contactId) throws NoSuchElementException {
        User user = repository.getUserByContactId(contactId);
        if (user == null) throw new NoSuchElementException(contactId + " does not exists!");
        try {
            return serializeUUIDToMessagePack(user.getUuid());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] serializeUUIDToMessagePack(UUID uuid) throws IOException {
        MessagePackFactory factory = new MessagePackFactory();
        ObjectMapper objectMapper = new ObjectMapper(factory);
        return objectMapper.writeValueAsBytes(uuid);
    }
}
