package com.jorji.chat.authenticationservice.services;

import com.jorji.chat.authenticationservice.model.User;
import com.jorji.chat.authenticationservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final HashingService hashingService;
    public boolean authenticate(String username, byte[] password){
        User user = repository.getUserByUsername(username);
        if (user == null) return false;
        return hashingService.verify(user.getPasswordBytes(), password, user.getSaltBytes());
    }
}
