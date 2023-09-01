package com.jorji.chatutil.userutil.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class FullUser {
    @Id
    private UUID uuid;
    private String username;
    private String password;
    private String salt;
    private Instant timestamp;
    private boolean isPrivate;
    private String contactId;

    public byte[] getPasswordBytes(){
        return Base64.getDecoder().decode(password);
    }

    public byte[] getSaltBytes(){
        return Base64.getDecoder().decode(salt);
    }
}
