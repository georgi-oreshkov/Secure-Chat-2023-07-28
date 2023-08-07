package com.jorji.chat.routingservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorji.chat.routingservice.model.ChatMessage;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SerializationService {
    private final ObjectMapper mapper;

    public SerializationService() {
        this.mapper = new ObjectMapper(new MessagePackFactory());
    }

    public <T> byte[] serialize(T message) throws JsonProcessingException {
        return this.mapper.writeValueAsBytes(message);
    }

    public <T> T deserialize(byte[] bytes, Class<T> valueType) throws IOException {
        return this.mapper.readValue(bytes, valueType);
    }

}
