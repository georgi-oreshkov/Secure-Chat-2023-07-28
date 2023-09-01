package com.jorji.chat.useridresolverservice.repositories;



import com.jorji.chatutil.userutil.model.FullUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResolutionUserRepository extends MongoRepository<FullUser, UUID> {
    FullUser getUserByUsername(String username);
    FullUser getUserByContactId(String contactId);

}
