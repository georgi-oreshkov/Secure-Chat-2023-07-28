package com.jorji.chat.routingservice.model;

import com.jorji.chatutil.userutil.model.SlimUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UserIdentifiers")
public class UserIdentifiers implements Serializable {
    @Id
    private UUID uuid;
    @Indexed
    private String username;
    @Indexed
    private String contactId;

    public static UserIdentifiers fromSlimUser(SlimUser user){
        return new UserIdentifiers(user.getUuid(), user.getUsername(), user.getContactId());
    }
}
