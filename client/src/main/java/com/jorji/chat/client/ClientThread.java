package com.jorji.chat.client;

import com.jorji.chat.client.model.ChatMessage;
import com.jorji.chat.client.websocket.SCBinaryWebsocketClient;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final String instanceName;
    public static final Object obj_lock = new Object();

    private final Integer msgCount;
    private final Integer clientCount;

    private final SCBinaryWebsocketClient client;

    public ClientThread(Map<String, Object> properties, int instId) throws FileNotFoundException {
        this.instanceName = "javaPerfAnalyzer-" + instId;

        try {
            URL authUrl = URI.create((String) properties.get("authenticate-url")).toURL();
            String uuid = authenticateAndGetUUID(authUrl);
            Map<String, String> headers = new HashMap<>();
            headers.put("X-User-Id", uuid);

            this.msgCount = (Integer) properties.get("message-count");
            this.clientCount = (Integer) properties.get("client-count");

            this.client = new SCBinaryWebsocketClient(
                    URI.create((String) properties.get("router-url")),
                    headers,
                    msgCount * clientCount);
            this.client.connectBlocking(10, TimeUnit.SECONDS);
            logger.info("State: {}", this.client.getReadyState());


        } catch (MalformedURLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < msgCount; i++) {
                for (int j = 0; j < clientCount; j++) {
                    ChatMessage chatMessage = new ChatMessage(
                            RandomStringGenerator.generateRandomString(50),
                            instanceName,
                            "javaPerfAnalyzer-" + j);
                    client.sendMessage(chatMessage);
                }
            }
            synchronized (obj_lock) {
                obj_lock.wait();
            }
            client.close();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    void register(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = String.format("""
                    {
                        "username": "%s",
                        "password": "123456789",
                        "prv": false
                    }""", instanceName);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(jsonPayload.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            logger.info("Registration status was {}", responseCode);
            if (responseCode != 200 && responseCode != 409) {
                throw new RuntimeException("Registration failed.");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public String authenticateAndGetUUID(URL url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = String.format("""
                    {
                        "username": "%s",
                        "password": "123456789"
                    }""", instanceName);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.write(jsonPayload.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();
            logger.info("The authentication code is {}", responseCode);

            if (responseCode != 200) {
                logger.error("Auth failed for {}", instanceName);
                System.exit(1);
            }

            byte[] responseBytes = connection.getInputStream().readAllBytes();
            connection.disconnect();
            try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(responseBytes)) {
                String uuidString = unpacker.unpackString();
                UUID uuid = UUID.fromString(uuidString);
                return uuid.toString();
            }
        } catch (
                Exception e) {
            throw new RuntimeException("Error sending POST request: " + e.getMessage(), e);
        }

    }
}