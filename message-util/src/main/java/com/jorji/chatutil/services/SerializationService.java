package com.jorji.chatutil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SerializationService {
    private final ObjectMapper mapper;

    public SerializationService() {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        objectMapper.registerModule(new JavaTimeModule()); // Register JSR310 module
        this.mapper = objectMapper;
    }

    public <T> byte[] serialize(T message) throws JsonProcessingException {
        return this.mapper.writeValueAsBytes(message);
    }

    public <T> T deserialize(byte[] bytes, Class<T> valueType) throws IOException {
        return this.mapper.readValue(bytes, valueType);
    }
}
