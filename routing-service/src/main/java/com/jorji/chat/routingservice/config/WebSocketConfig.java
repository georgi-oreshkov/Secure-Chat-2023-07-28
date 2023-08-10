package com.jorji.chat.routingservice.config;

import com.jorji.chat.routingservice.config.handshake.UUIDResolvingHandshakeInterceptor;
import com.jorji.chat.routingservice.handlers.CustomBinaryWebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final UUIDResolvingHandshakeInterceptor interceptor;
    private final CustomBinaryWebSocketHandler myHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler, "/router")
                .setAllowedOriginPatterns("*")
                .addInterceptors(interceptor);
    }
}
