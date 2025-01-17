package com.jorji.chat.routingservice.config.handshake;

import com.jorji.chat.routingservice.config.UserResolverServiceProperties;
import com.jorji.chatutil.userutil.model.SlimUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class UUIDResolvingHandshakeInterceptor implements HandshakeInterceptor {
    private final RestTemplate template;
    private final UserResolverServiceProperties resolverServiceProperties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, @NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId == null) return false;
        attributes.put("user", fetchUser(userId));
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler, Exception ex) {
        // You can perform post-processing here after the handshake is successful.
    }

    private SlimUser fetchUser(String uuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", uuid);

        ResponseEntity<SlimUser> response = template.exchange(
                resolverServiceProperties.getHost() +
                ":" + resolverServiceProperties.getPort() +
                resolverServiceProperties.getApiPath(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                SlimUser.class
        );

        if (!response.getStatusCode().is2xxSuccessful())
            throw new NoSuchElementException(response.getStatusCode().toString());

        return response.getBody();
    }
}
