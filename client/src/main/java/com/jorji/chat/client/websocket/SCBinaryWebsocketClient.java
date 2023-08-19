package com.jorji.chat.client.websocket;

import com.jorji.chat.client.ClientThread;
import com.jorji.chat.client.Main;
import com.jorji.chat.client.model.ChatMessage;
import com.jorji.chat.client.model.ChatMessageSerializer;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SCBinaryWebsocketClient extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final ChatMessageSerializer serializer;
    long messagesReceived;

    long fMessage;

    Integer expected;

    public SCBinaryWebsocketClient(URI uri, Map<String, String> headers, Integer expectedMsg) {
        super(uri, new Draft_6455(), headers);
        this.serializer = new ChatMessageSerializer();
        this.messagesReceived = 0;
        this.expected = expectedMsg;
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
        if (messagesReceived == 0) fMessage = System.currentTimeMillis();
        messagesReceived++;
        ChatMessage message = this.serializer.deserialize(bytes.array());
        logger.info("{}, From: {}, Body: {}, Type: {}",
                DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                        .format(LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(message.getTime()),
                                ZoneId.systemDefault()
                        )),
                message.getSender(),
                message.getContent(),
                message.getType());

        if (messagesReceived >= expected) {
            logger.info("Received {}/{} in {} seconds",
                    messagesReceived,
                    expected,
                    (double) (System.currentTimeMillis() - fMessage) / 1000);
            synchronized (ClientThread.obj_lock) {
                ClientThread.obj_lock.notifyAll();
            }
        }
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
        byte[] message = serializer.serialize(chatMessage);
        ByteBuffer buffer = ByteBuffer.wrap(message);
        this.send(buffer);
    }


}
