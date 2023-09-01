package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.config.AmqpProperties;
import com.jorji.chatutil.model.ChatMessage;
import com.jorji.chatutil.model.MessageType;
import com.jorji.chatutil.services.SerializationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RouterService {
    private static final Logger logger = LoggerFactory.getLogger(RouterService.class);
    private final RabbitTemplate rabbitTemplate;

    private final SerializationService serializationService;

    private final AmqpProperties amqpProperties;

    private final AbstractMap<UUID, WebSocketSession> sessionMap;

    private final UserResolverService resolverService;



    public void handleMessage(byte[] message) throws IOException {
        ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
        MessageType type = chatMessage.getType();

        switch (type) {
            case CHAT -> {
                try {
                    chatMessage = resolverService.resolveDestUsername(chatMessage);
                    sendMessage(chatMessage);
                } catch (NoSuchElementException e){
                    scheduleDelayedMessage(chatMessage);
                }

            }
            case CHAT_PRIVATE -> {
                try {
                    chatMessage = resolverService.resolveDestContactId(chatMessage);
                    sendMessage(chatMessage);
                } catch (NoSuchElementException e){
                    scheduleDelayedMessage(chatMessage);
                }
            }
            case CHAT_GROUP -> {
                String queueName = amqpProperties.getGroupResolutionQueueName();
                Message amqpMessage = MessageBuilder
                        .withBody(message)
                        .build();
                logger.info("Sending message to `{}`", queueName);
                rabbitTemplate.send(queueName, amqpMessage);
            }
            default -> throw new IllegalArgumentException("Message type: " + type + "is illegal.");
        }
    }

    public void scheduleDelayedMessage(ChatMessage message) {
        // todo
    }

    @RabbitListener(queues = "${com.jorji.chat.amqp.direct-message-queue-name}")
    public void receiveRabbitMessage(byte[] message) throws IOException {
        ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
        sendMessage(chatMessage);
    }


    public void sendMessage(ChatMessage message) {
        try {
            WebSocketSession session = sessionMap.get(
                    UUID.fromString(message.getDestination()));

            if (session == null) {
                scheduleDelayedMessage(message);
                return;
            }

            BinaryMessage binaryMessage = new BinaryMessage(serializationService.serialize(message));

            synchronized (session) {
                session.sendMessage(binaryMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
