package com.jorji.chat.authenticationservice.controller;

import com.jorji.chat.authenticationservice.model.LoginRequest;
import com.jorji.chat.authenticationservice.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/authenticate")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    @PostMapping
    public ResponseEntity<byte[]> authenticate(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Received request: " + loginRequest.getUsername() + " " + loginRequest.getPassword());
            byte[] uuid = authenticationService.authenticate(
                    loginRequest.getUsername(),
                    loginRequest.getPassword().getBytes(StandardCharsets.UTF_8));
            logger.info("Success.");
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/x-msgpack")
                    .body(uuid);

        } catch (NoSuchElementException e){
            logger.info("NoSuchElementException.");
            return ResponseEntity.notFound().build();
        } catch (AuthenticationException e) {
            logger.info("AuthenticationException: " + e.getMessage());
            return ResponseEntity.status(401).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
