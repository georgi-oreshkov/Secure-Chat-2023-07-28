package com.jorji.chat.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jorji.chat.client.Main;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChatMessageSerializer {
    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public ChatMessageSerializer() {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        objectMapper.registerModule(new JavaTimeModule()); // Register JSR310 module
        this.mapper = objectMapper;
    }

    public byte[] serialize(ChatMessage chatMessage) {
        try {
            return this.mapper.writeValueAsBytes(chatMessage);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ChatMessage deserialize(byte[] bytes) {
        try {
            return this.mapper.readValue(bytes, ChatMessage.class);
        } catch (IOException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
