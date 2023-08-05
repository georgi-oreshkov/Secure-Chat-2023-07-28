package com.jorji.chat.usermanagementservice.controllers;

import com.jorji.chat.usermanagementservice.model.RegisterRequest;
import com.jorji.chat.usermanagementservice.model.RegisterResponse;
import com.jorji.chat.usermanagementservice.services.UserManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.openmbean.KeyAlreadyExistsException;

@RestController
@RequestMapping("/api/")
@AllArgsConstructor
public class UserManagementController {
    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = userManagementService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (KeyAlreadyExistsException e){
            return ResponseEntity.status(409).body("User already exists.");
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
