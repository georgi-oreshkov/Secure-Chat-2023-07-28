package com.jorji.chat.authenticationservice.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {
    private String username;
    private String password;
    private String salt;

    public byte[] getPasswordBytes() {
        return Base64.decodeBase64(this.password);
    }

    public byte[] getSaltBytes() {
        return Base64.decodeBase64(this.salt);
    }
}
