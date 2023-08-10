package com.jorji.chat.routingservice.handlers;

import com.jorji.chat.routingservice.services.RouterService;
import com.jorji.chatutil.model.MessageType;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.nio.ByteBuffer;
import java.util.AbstractMap;

@Component
@AllArgsConstructor
public class CustomBinaryWebSocketHandler extends BinaryWebSocketHandler {
    private final AbstractMap<String, WebSocketSession> sessionMap;
    private final RouterService routerService;


    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        this.sessionMap.put(userId, session);
    }

    @Override
    protected void handleBinaryMessage(@NonNull WebSocketSession session, @NonNull BinaryMessage binMessage) throws Exception {
        ByteBuffer buffer = binMessage.getPayload();
        // header is 6 bytes total.
        int payloadLength = buffer.getInt(); // read exactly 4 bytes
        short code = buffer.getShort(); // read 2 bytes

        byte[] message = new byte[payloadLength];
        buffer.get(message);

        routerService.sendMessage(message, MessageType.fromCode(code));
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        if (!sessionMap.remove(userId, session))
            throw new RuntimeException("Session " + userId + " removal failed!");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
