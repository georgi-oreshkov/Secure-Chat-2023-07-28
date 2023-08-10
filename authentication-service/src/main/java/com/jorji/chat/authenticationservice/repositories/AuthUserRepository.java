package com.jorji.chat.authenticationservice.repositories;

import com.jorji.chatutil.userutil.model.FullUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthUserRepository extends MongoRepository<FullUserModel, UUID> {
    FullUserModel getUserByUsername(String username);
}
