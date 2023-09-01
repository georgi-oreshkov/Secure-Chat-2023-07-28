package com.jorji.chat.authenticationservice.services;


import com.jorji.chat.authenticationservice.repositories.AuthUserRepository;
import com.jorji.chatutil.userutil.model.FullUser;
import com.jorji.chatutil.userutil.services.UserUtilsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthUserRepository repository;
    private final UserUtilsService userUtilsService;
    private final HashingService hashingService;

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public byte[] authenticate(String username, byte[] password) throws NoSuchElementException, AuthenticationException, IOException {
        logger.info("Authenticating for " + username + " with password: " + Base64.getEncoder().encodeToString(password));
        FullUser user = repository.getUserByUsername(username);

        if (user == null) {
            logger.info("Couldn't find user.");
            throw new NoSuchElementException();
        }
        logger.info("User found. Username: {}, Pass: {}, Salt: {}",
                user.getUsername(),
                user.getPassword(),
                user.getSalt());

        boolean verified = hashingService.verify(
                user.getPasswordBytes(),
                password,
                user.getSaltBytes());
        if (!verified){
            logger.info("Verification failed.");
            throw new AuthenticationException();
        }

        logger.info("All went well.");
        return userUtilsService.serialize(user.getUuid());
    }
}
