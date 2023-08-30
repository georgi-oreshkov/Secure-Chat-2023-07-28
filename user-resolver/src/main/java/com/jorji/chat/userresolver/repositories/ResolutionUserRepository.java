package com.jorji.chat.userresolver.repositories;



import com.jorji.chatutil.userutil.model.FullUserModel;
import com.jorji.chatutil.userutil.model.ResolverUserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResolutionUserRepository extends MongoRepository<ResolverUserModel, UUID> {
    ResolverUserModel getResolverUserModelByUuid(UUID uuid);

}
