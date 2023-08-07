package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.config.UserResolverServiceProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UUIDResolverService {
    private RestTemplate restTemplate;
    private final UserResolverServiceProperties properties;

    public UUID getUUIDofUser(String username) throws NoSuchElementException {
        ResponseEntity<UUID> response = restTemplate.getForEntity(properties.getUrlForUser(username), UUID.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new NoSuchElementException("Resolver service returned code: " + response.getStatusCode());

        return response.getBody();
    }

    public UUID getUUIDofContact(String contactId) throws NoSuchElementException {
        ResponseEntity<UUID> response = restTemplate.getForEntity(properties.getUrlForContact(contactId), UUID.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new NoSuchElementException("Resolver service returned code: " + response.getStatusCode());

        return response.getBody();
    }
}
