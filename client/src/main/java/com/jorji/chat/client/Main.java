package com.jorji.chat.client;

import com.jorji.chat.client.model.ChatMessage;
import com.jorji.chat.client.websocket.CustomWebSocketConfigurator;
import com.jorji.chat.client.websocket.SCBinaryWebsocketClient;
import jakarta.websocket.*;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {
//    static String REGISTRATION_URL = "http://localhost:9001/api/register";
//    static String AUTHENTICATION_URL = "http://localhost:9000/api/authenticate";
//    static String WEBSOCKET_URL = "ws://localhost:9004/router";

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String instanceName;

    public static void main(String[] args) {
        String yamlFilePath;
        if (args.length == 0){
            logger.info("No yml settings file specified. Using default settings.");
            instanceName = "javaTest0";
            yamlFilePath = "client/src/main/resources/client_settings.yml";
        } else if (args.length == 2){
            instanceName = args[0];
            yamlFilePath = args[1];
        } else {
            throw new RuntimeException();
        }


        try {
            Yaml yaml = new Yaml();
            FileInputStream inputStream = new FileInputStream(yamlFilePath);
            Map<String, Object> properties = yaml.load(inputStream);
            inputStream.close();

            URL registerUrl = URI.create((String) properties.get("register-url")).toURL();
            register(registerUrl);

            URL authUrl = URI.create((String) properties.get("authenticate-url")).toURL();
            String uuid = authenticateAndGetUUID(authUrl);

            SCBinaryWebsocketClient client = new SCBinaryWebsocketClient();

            try (Session ignore = connect(
                    URI.create((String) properties.get("router-url")),
                    uuid,
                    client)) {
                for (int i = 0; i < 5; i++) {
                    ChatMessage chatMessage = new ChatMessage("Hello, world", instanceName, instanceName);
                    client.sendMessage(chatMessage);
                }
                Thread.sleep(10000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    static void register(URL url) {
        assert instanceName != null;
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

    public static String authenticateAndGetUUID(URL url) {
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
                throw new RuntimeException("Auth failed.");
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

    static Session connect(URI uri, String uuid, Endpoint client) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        // Set custom headers
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-User-Id", uuid);

        // Create a ClientEndpointConfig with custom headers
        ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
                .configurator(new CustomWebSocketConfigurator(customHeaders))
                .build();

        try {
            return container.connectToServer(
                    client,
                    clientConfig,
                    uri);
        } catch (DeploymentException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


}