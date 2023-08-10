package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.config.AmqpConfig;
import com.jorji.chatutil.model.ChatMessage;
import com.jorji.chatutil.model.MessageType;
import com.jorji.chatutil.services.SerializationService;
import lombok.AllArgsConstructor;
import org.msgpack.core.MessageFormatException;
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
    private final RabbitTemplate rabbitTemplate;

    private final SerializationService serializationService;

    private final AmqpConfig amqpConfig;

    private final AbstractMap<String, WebSocketSession> sessionMap;


    public void sendMessage(byte[] message, MessageType type) {
        if (!serializationService.isValidMsg(message, ChatMessage.class))
            throw new MessageFormatException("Message can't be deserialized!");
        Message amqpMessage = MessageBuilder
                .withBody(message)
                .build();
        String queueName;
        switch (type) {
            case CHAT -> queueName = amqpConfig.getUserResolutionQueueName();
            case CHAT_GROUP -> queueName = amqpConfig.getGroupResolutionQueueName();
            case CHAT_PRIVATE -> queueName = amqpConfig.getContactResolutionQueueName();
            default -> throw new IllegalArgumentException("Message type: " + type + "is illegal.");
        }
        rabbitTemplate.send(queueName, amqpMessage);
    }

    public void scheduleDelayedMessage(byte[] message) {
        System.out.println("A message was delayed!");
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
