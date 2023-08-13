package com.jorji.chat.routingservice.handlers;

import com.jorji.chat.routingservice.services.RouterService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.ByteBuffer;
import java.util.AbstractMap;

@Component
@AllArgsConstructor
public class CustomBinaryWebSocketHandler extends BinaryWebSocketHandler {
    private final AbstractMap<String, WebSocketSession> sessionMap;
    private final RouterService routerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomBinaryWebSocketHandler.class);


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        logger.info("Connection established for " + userId);
        this.sessionMap.put(userId, session);
    }

    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage binMessage) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        logger.info("Message received from {}", userId);
        ByteBuffer buffer = binMessage.getPayload();
        byte[] message = new byte[binMessage.getPayloadLength()];
        buffer.get(message);

        routerService.sendMessage(message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        logger.info("Connection closed for {}", userId);
        if (!sessionMap.remove(userId, session))
            throw new RuntimeException("Session " + userId + " removal failed!");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
