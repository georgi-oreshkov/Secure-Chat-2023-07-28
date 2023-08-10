package com.jorji.chat.useridresolverservice.repositories;



import com.jorji.chatutil.userutil.model.FullUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResolutionUserRepository extends MongoRepository<FullUserModel, UUID> {
    FullUserModel getUserByUsername(String username);
    FullUserModel getUserByContactId(String contactId);

}
