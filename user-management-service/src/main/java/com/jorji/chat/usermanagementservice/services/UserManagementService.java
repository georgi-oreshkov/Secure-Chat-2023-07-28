package com.jorji.chat.usermanagementservice.services;

import com.jorji.chat.usermanagementservice.model.RegisterRequest;
import com.jorji.chat.usermanagementservice.model.RegisterResponse;
import com.jorji.chat.usermanagementservice.repositories.FullUserRepository;
import com.jorji.chatutil.userutil.model.FullUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserManagementService {
    private final HashingService hashingService;
    private final FullUserRepository repository;

    public RegisterResponse registerUser(RegisterRequest registerRequest) throws KeyAlreadyExistsException {
        if (repository.existsByUsername(registerRequest.getUsername())) throw new KeyAlreadyExistsException();
        String contactId = hashingService.generateContactId();
        FullUser user = hashingService.getUserBuilderWithPass(registerRequest.getPassword().getBytes())
                .uuid(UUID.randomUUID())
                .username(registerRequest.getUsername())
                .contactId(contactId)
                .isPrivate(registerRequest.isPrv())
                .timestamp(Instant.now())
                .build();

        repository.save(user);
        return new RegisterResponse(registerRequest.getUsername(), contactId);
    }


}
