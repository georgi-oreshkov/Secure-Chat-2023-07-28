package com.jorji.chatutil.userutil.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Getter
@Setter
@Builder
@Document(collection = "users")
public class ResolverUserModel {
    @Id
    private UUID uuid;

    private String username;
    private String contactId;
}
