package com.jorji.chatutil.userutil.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserUtilsService {

    private final ObjectMapper objectMapper;

    public UserUtilsService() {
        MessagePackFactory factory = new MessagePackFactory();
        this.objectMapper = new ObjectMapper(factory);
    }

    public byte[] serialize(UUID uuid) throws IOException {
        return objectMapper.writeValueAsBytes(uuid);
    }
}
