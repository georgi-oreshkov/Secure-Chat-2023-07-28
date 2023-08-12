package com.jorji.chat.client.websocket;

import com.jorji.chat.client.model.ChatMessageSerializer;
import com.jorji.chat.client.model.ChatMessage;
import jakarta.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.Arrays;

@ClientEndpoint
public class SCBinaryWebsocketClient extends Endpoint{
    private static final Logger logger = LoggerFactory.getLogger(SCBinaryWebsocketClient.class);
    private static final Logger performance_logger = LoggerFactory.getLogger("performance-logger");
    private Session session;
    int messagesReceived;

    public SCBinaryWebsocketClient() {
        this.messagesReceived = 0;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        logger.info("Connection established.");
    }

    @OnMessage
    public void handleBinaryMessage(byte[] message, Session session) {
        messagesReceived++;
        ChatMessage chatMessage = ChatMessageSerializer.deserialize(message);
        performance_logger.info("{}: {}", Instant.now().toEpochMilli(), chatMessage.toLogString());
    }

    public void sendMessage(ChatMessage chatMessage) {
        try {
            byte[] message = ChatMessageSerializer.serialize(chatMessage);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            this.session.getBasicRemote().sendBinary(buffer);
        } catch (IOException e) {
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Connection closed.");
    }
}
