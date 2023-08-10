package com.jorji.chat.useridresolverservice.amqp;


import com.jorji.chat.useridresolverservice.services.ResolverService;
import com.jorji.chatutil.model.ChatMessage;
import com.jorji.chatutil.services.SerializationService;
import com.jorji.chatutil.userutil.exceptions.PrivateUserException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RabbitMQListener {
    private final RabbitTemplate rabbitTemplate;
    private final SerializationService serializationService;
    private final ResolverService resolverService;

    private final AmqpConfig config;

    @RabbitListener(queues = "${com.jorji.chat.amqp.user-resolution-queue-name}")
    public void receiveRabbitUserMessage(byte[] message) {
        ChatMessage chatMessage;
        try {
            chatMessage = serializationService.deserialize(message, ChatMessage.class);
            UUID uuid = resolverService.getUUIDofUser(chatMessage.getDestination());

            chatMessage.setDestination(uuid.toString());

            byte[] chatMessagePayload = serializationService.serialize(chatMessage);
            Message amqpMessage = MessageBuilder.withBody(chatMessagePayload).build();
            rabbitTemplate.send(config.getDirectMessageQueueName(), amqpMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PrivateUserException ignore) {
        }
    }

    @RabbitListener(queues = "${com.jorji.chat.amqp.contact-resolution-queue-name}")
    public void receiveRabbitContactMessage(byte[] message) {
        ChatMessage chatMessage;
        try {
            chatMessage = serializationService.deserialize(message, ChatMessage.class);
            UUID uuid = resolverService.getUUIDofContact(chatMessage.getDestination());

            chatMessage.setDestination(uuid.toString());

            byte[] chatMessagePayload = serializationService.serialize(chatMessage);
            Message amqpMessage = MessageBuilder.withBody(chatMessagePayload).build();
            rabbitTemplate.send(config.getDirectMessageQueueName(), amqpMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
