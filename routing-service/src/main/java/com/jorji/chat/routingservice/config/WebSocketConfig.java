package com.jorji.chat.routingservice.config;

import com.jorji.chat.routingservice.config.handshake.UUIDResolvingHandshakeInterceptor;
import com.jorji.chat.routingservice.handlers.CustomBinaryWebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

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

    @Bean
    public AbstractMap<String, WebSocketSession> getSessionMap(){
        return new ConcurrentHashMap<>();
    }
}
