package com.jorji.chat.routingservice.handlers;

import com.jorji.chat.routingservice.model.User;
import com.jorji.chat.routingservice.services.RouterService;
import com.jorji.chatutil.userutil.model.ResolverUserModel;
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
import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomBinaryWebSocketHandler extends BinaryWebSocketHandler {
    private final AbstractMap<UUID, WebSocketSession> sessionMap;
    private final RouterService routerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomBinaryWebSocketHandler.class);


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        ResolverUserModel user = (ResolverUserModel) session.getAttributes().get("user");
        logger.info("Connection established for " + user);
        this.sessionMap.put(user, session);
    }

    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage binMessage) throws Exception {
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
        User user = (User) session.getAttributes().get("user");
        logger.info("Connection closed for {}", user);
        if (!sessionMap.remove(user, session))
            throw new RuntimeException("Session " + user.getUuid() + " removal failed!");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
