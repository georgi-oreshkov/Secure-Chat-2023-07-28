package com.jorji.chat.usermanagementservice.repositories;

import com.jorji.chatutil.userutil.model.FullUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FullUserRepository extends MongoRepository<FullUserModel, UUID> {
    boolean existsByUsername(String username);
}
