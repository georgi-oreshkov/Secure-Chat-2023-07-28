package com.jorji.chat.authorization_service.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private UUID uuid;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String salt;
    private Instant timestamp;

    public byte[] getPasswordBytes(){
        return Base64.decodeBase64(this.password);
    }

    public byte[] getSaltBytes(){
        return Base64.decodeBase64(this.salt);
    }
}
