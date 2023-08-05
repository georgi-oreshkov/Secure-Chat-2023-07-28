package com.jorji.chat.usermanagementservice.repository;

import com.jorji.chat.usermanagementservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    boolean existsByUsername(String username);
}
