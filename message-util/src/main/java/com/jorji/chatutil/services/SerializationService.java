package com.jorji.chatutil.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SerializationService {
    private final ObjectMapper mapper;

    public SerializationService(@Qualifier("msgpack-object-mapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public <T> byte[] serialize(T message) throws JsonProcessingException {
        return this.mapper.writeValueAsBytes(message);
    }

    public <T> T deserialize(byte[] bytes, Class<T> valueType) throws IOException {
        return this.mapper.readValue(bytes, valueType);
    }

    public <T> boolean isValidMsg(byte[] bytes, Class<T> valueType){
        try {
            deserialize(bytes, valueType);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
