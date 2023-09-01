package com.jorji.chat.routingservice.handlers;

import com.jorji.chat.routingservice.model.UserIdentifiers;
import com.jorji.chat.routingservice.repositories.UserIdentifiersRepository;
import com.jorji.chat.routingservice.services.RouterService;
import com.jorji.chatutil.userutil.model.SlimUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CustomBinaryWebSocketHandler extends BinaryWebSocketHandler {
    private final AbstractMap<UUID, WebSocketSession> sessionMap;
    private final RouterService routerService;
    private final UserIdentifiersRepository identifiersRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomBinaryWebSocketHandler.class);


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        SlimUser user = (SlimUser) session.getAttributes().get("user");
        UserIdentifiers userIdentifiers = UserIdentifiers.fromSlimUser(user);
        session.getAttributes().put("user", userIdentifiers);

        logger.info("Connection established for " + user);
        this.sessionMap.put(user.getUuid(), session);
        this.identifiersRepository.save(userIdentifiers);
    }

    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage binMessage) throws IOException {
        ByteBuffer buffer = binMessage.getPayload();
        byte[] message = new byte[binMessage.getPayloadLength()];
        buffer.get(message);

        routerService.handleMessage(message);
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        UserIdentifiers userIdentifiers = (UserIdentifiers) session.getAttributes().get("user");
        logger.info("Connection closed for {}", userIdentifiers.getUsername());
        this.identifiersRepository.delete(userIdentifiers);
        if (!sessionMap.remove(userIdentifiers.getUuid(), session))
            throw new RuntimeException("Session " + userIdentifiers.getUuid() + " removal failed!");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
