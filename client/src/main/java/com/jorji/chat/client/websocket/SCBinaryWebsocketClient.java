package com.jorji.chat.client.websocket;

import com.jorji.chat.client.Main;
import com.jorji.chat.client.model.ChatMessage;
import com.jorji.chat.client.model.ChatMessageSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class SCBinaryWebsocketClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
//    private static final Logger logger = LoggerFactory.getLogger("performance-logger");
    private final ChatMessageSerializer serializer;
    int messagesReceived;


    public SCBinaryWebsocketClient(URI uri) {
        super(uri, new Draft_6455());
        this.serializer = new ChatMessageSerializer();
        this.messagesReceived = 0;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("Connection established: {}", serverHandshake.getHttpStatus());
    }

    @Override
    public void onMessage(String s) {
        logger.info("Text message received: {}", s);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("Connection closed {}, reason: {}, remote: {}", i, s, b);
    }

    @Override
    public void onError(Exception e) {
        throw new RuntimeException(e);
    }


    public void sendMessage(ChatMessage chatMessage) {
        logger.info("Sending message.");
        try {
            byte[] message = serializer.serialize(chatMessage);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            this.session.getBasicRemote().sendBinary(buffer);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


}
