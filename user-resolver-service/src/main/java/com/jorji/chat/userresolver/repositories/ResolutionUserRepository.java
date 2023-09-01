package com.jorji.chat.userresolver.repositories;



import com.jorji.chatutil.userutil.model.SlimUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResolutionUserRepository extends MongoRepository<SlimUser, UUID> {
    SlimUser getSlimUserByUuid(UUID uuid);

}
