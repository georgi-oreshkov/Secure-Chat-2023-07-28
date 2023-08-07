package com.jorji.chat.useridresolverservice.controllers;

import com.jorji.chat.useridresolverservice.services.ResolverService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/resolve")
@AllArgsConstructor
public class ResolverController {
    private final ResolverService resolverService;
    @GetMapping("/username/{username}")
    public ResponseEntity<byte[]> getUUIDbyUsername(@PathVariable String username){
        try {
            byte[] serializedUUID = resolverService.getSerializedUUIDofUser(username);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/x-msgpack")
                    .body(serializedUUID);

        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<byte[]> getUUIDbyContact(@PathVariable String contactId){
        try {
            byte[] serializedUUID = resolverService.getSerializedUUIDofContact(contactId);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/x-msgpack")
                    .body(serializedUUID);
        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }
}
