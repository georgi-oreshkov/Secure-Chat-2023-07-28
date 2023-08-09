package com.jorji.chatutil.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {
    @Bean(name = "msgpack-object-mapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        objectMapper.registerModule(new JavaTimeModule()); // Register JSR310 module
        return objectMapper;
    }
}
