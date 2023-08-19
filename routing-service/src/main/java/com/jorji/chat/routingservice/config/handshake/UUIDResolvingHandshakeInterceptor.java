package com.jorji.chat.routingservice.config.handshake;

import com.jorji.chat.routingservice.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@AllArgsConstructor
public class UUIDResolvingHandshakeInterceptor implements HandshakeInterceptor {
    private final RestTemplate template;

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

    private User fetchUser(String uuid){

    }
}
