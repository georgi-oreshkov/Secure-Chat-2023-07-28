package com.jorji.chat.authorization_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "chat.authentication.argon2")
@Getter
@Setter
public class Argon2Properties {
    private int hashLengthBytes;
    private int parallelism;
    private int memoryKb;
    private int iterations;
}
