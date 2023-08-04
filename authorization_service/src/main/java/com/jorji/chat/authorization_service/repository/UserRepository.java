package com.jorji.chat.authorization_service.repository;

import com.jorji.chat.authorization_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    User getUserByUsername(String username);
}
