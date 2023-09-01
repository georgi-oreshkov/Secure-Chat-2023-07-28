package com.jorji.chat.authenticationservice.repositories;

import com.jorji.chatutil.userutil.model.FullUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthUserRepository extends MongoRepository<FullUser, UUID> {
    FullUser getUserByUsername(String username);
}
