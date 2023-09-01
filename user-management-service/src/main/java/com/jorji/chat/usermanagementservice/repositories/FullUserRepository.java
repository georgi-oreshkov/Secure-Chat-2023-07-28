package com.jorji.chat.usermanagementservice.repositories;

import com.jorji.chatutil.userutil.model.FullUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FullUserRepository extends MongoRepository<FullUser, UUID> {
    boolean existsByUsername(String username);
}
