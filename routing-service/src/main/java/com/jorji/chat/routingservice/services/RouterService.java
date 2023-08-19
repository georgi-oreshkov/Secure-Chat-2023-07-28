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

@Service
@AllArgsConstructor
public class RouterService {
    private static final Logger logger = LoggerFactory.getLogger(RouterService.class);
    private final RabbitTemplate rabbitTemplate;

    private final SerializationService serializationService;

    private final AmqpProperties amqpProperties;

    private final AbstractMap<String, WebSocketSession> sessionMap;


    public void sendMessage(byte[] message) throws IOException {
        ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
        Message amqpMessage = MessageBuilder
                .withBody(message)
                .build();
        String queueName;
        MessageType type = chatMessage.getType();
        switch (type) {
            case CHAT -> queueName = amqpProperties.getUserResolutionQueueName();
            case CHAT_GROUP -> queueName = amqpProperties.getGroupResolutionQueueName();
            case CHAT_PRIVATE -> queueName = amqpProperties.getContactResolutionQueueName();
            default -> throw new IllegalArgumentException("Message type: " + type + "is illegal.");
        }
        logger.info("Sending message to `{}`", queueName);
        rabbitTemplate.send(queueName, amqpMessage);
    }

    public void scheduleDelayedMessage(byte[] message) {
        // todo
    }

    @RabbitListener(queues = "${com.jorji.chat.amqp.direct-message-queue-name}")
    public void receiveRabbitMessage(byte[] message) {
        try {
            ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
            WebSocketSession session = sessionMap.get(chatMessage.getDestination());

            if (session == null) {
                scheduleDelayedMessage(message);
                return;
            }

            BinaryMessage binaryMessage = new BinaryMessage(message);
            session.sendMessage(binaryMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
