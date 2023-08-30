package com.jorji.chat.userresolver.controllers;

import com.jorji.chat.userresolver.services.ResolverService;
import com.jorji.chatutil.userutil.model.ResolverUserModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/resolve")
public class ResolverController {
    ResolverService service;

    @GetMapping
    public ResponseEntity<ResolverUserModel> getUserByUUID(@RequestHeader("X-User-Id") String uuid) {
        UUID uuid1 = UUID.fromString(uuid);
        try {
            return ResponseEntity.ok(service.getUserByUUID(uuid1));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
