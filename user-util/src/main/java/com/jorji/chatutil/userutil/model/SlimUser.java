package com.jorji.chatutil.userutil.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class SlimUser {
    @Id
    private UUID uuid;

    private String username;
    private String contactId;
}
