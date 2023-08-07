package com.jorji.chat.routingservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jorji.chat.routingservice.config.AmqpConfig;
import com.jorji.chat.routingservice.model.ChatMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class RouterService {
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private final SerializationService serializationService;

    private final AmqpConfig amqpConfig;


    public void sendDirectMessage(ChatMessage message){
        try{
            Message amqpMessage = MessageBuilder
                    .withBody(serializationService.serialize(message))
                    .build();
            rabbitTemplate.send(amqpConfig.getUserResolutionQueueName(), amqpMessage);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendGroupMessage(ChatMessage message) {
        try {
            Message amqpMessage = MessageBuilder
                    .withBody(serializationService.serialize(message))
                    .build();
            rabbitTemplate.send(amqpConfig.getGroupResolutionQueueName(), amqpMessage);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @RabbitListener(queues = "${com.jorji.chat.amqp.direct-message-queue-name}")
    public void receiveRabbitMessage(byte[] message){
        try {
            ChatMessage chatMessage = serializationService.deserialize(message, ChatMessage.class);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getDestination(),
                    "/direct/" + chatMessage.getSender(),
                    serializationService.serialize(chatMessage)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
