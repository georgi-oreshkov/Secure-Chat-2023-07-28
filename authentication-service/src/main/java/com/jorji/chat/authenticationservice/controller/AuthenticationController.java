package com.jorji.chat.authenticationservice.controller;

import com.jorji.chat.authenticationservice.model.LoginRequest;
import com.jorji.chat.authenticationservice.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/authenticate")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Void> authenticate(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword().getBytes(StandardCharsets.UTF_8));
        if(!authenticated) return ResponseEntity.status(401).build();
        return ResponseEntity.status(200).build();
    }
}
