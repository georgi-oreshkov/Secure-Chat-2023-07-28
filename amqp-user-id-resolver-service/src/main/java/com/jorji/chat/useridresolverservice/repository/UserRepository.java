package com.jorji.chat.useridresolverservice.repository;


import com.jorji.chat.useridresolverservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {
    User getUserByUsername(String username);
    User getUserByContactId(String contactId);

}
