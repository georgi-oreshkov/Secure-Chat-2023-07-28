package com.jorji.chat.routingservice.config;

import com.jorji.chat.routingservice.config.handshake.UUIDResolvingHandshakeInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UUIDResolvingHandshakeInterceptor interceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/router")
                .setAllowedOriginPatterns("*")
                .addInterceptors(interceptor);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/message");
    }
}
