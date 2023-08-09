package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.config.AmqpConfig;
import com.jorji.chatutil.model.ChatMessage;
import com.jorji.chatutil.services.SerializationService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
@AllArgsConstructor
public class RouterService {
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private final SerializationService serializationService;

    private final AmqpConfig amqpConfig;


    public void sendDirectMessage(byte[] bytes) {
        byte[] message = Base64.getDecoder().decode(bytes);
        Message amqpMessage = MessageBuilder
                .withBody(message)
                .build();
        rabbitTemplate.send(amqpConfig.getUserResolutionQueueName(), amqpMessage);

    }

    public void sendGroupMessage(byte[] bytes) {
        byte[] message = Base64.getDecoder().decode(bytes);
        Message amqpMessage = MessageBuilder
                .withBody(message)
                .build();
        rabbitTemplate.send(amqpConfig.getUserResolutionQueueName(), amqpMessage);
    }

    @RabbitListener(queues = "${com.jorji.chat.amqp.direct-message-queue-name}")
    public void receiveRabbitMessage(byte[] message) {
        try {
            ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getDestination(),
                    "/direct",
                    serializationService.serialize(chatMessage)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
