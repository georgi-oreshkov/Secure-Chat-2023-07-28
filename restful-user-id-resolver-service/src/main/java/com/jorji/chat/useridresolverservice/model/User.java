package com.jorji.chat.useridresolverservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private UUID uuid;
    private String username;
    private String contactId;
}
