package com.jorji.chat.routingservice.repositories;

import com.jorji.chat.routingservice.model.UserIdentifiers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserIdentifiersRepository extends CrudRepository<UserIdentifiers, UUID> {
    Optional<UserIdentifiers> findUserIdentifiersByUsername(String username);
    Optional<UserIdentifiers> findUserIdentifiersByContactId(String cid);
}
