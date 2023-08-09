package com.jorji.chat.useridresolverservice.amqp;


import com.jorji.chat.useridresolverservice.exceptions.PrivateUserException;
import com.jorji.chat.useridresolverservice.services.ResolverService;

import com.jorji.chatutil.model.ChatMessage;
import com.jorji.chatutil.services.SerializationService;
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
    public void receiveRabbitMessage(byte[] message) {
        ChatMessage chatMessage;
        try {
            chatMessage = serializationService.deserialize(message, ChatMessage.class);
            byte[] uuidBytes;
            switch (chatMessage.getType()) {
                case CHAT -> uuidBytes = resolverService.getSerializedUUIDofUser(chatMessage.getDestination());
                case CHAT_PRIVATE ->
                        uuidBytes = resolverService.getSerializedUUIDofContact(chatMessage.getDestination());
                default ->
                        throw new UnsupportedOperationException("Invalid chat message type: " + chatMessage.getType());
            }
            UUID uuid = serializationService.deserialize(uuidBytes, UUID.class);
            chatMessage.setDestination(uuid.toString());

            byte[] chatMessagePayload = serializationService.serialize(chatMessage);
            Message amqpMessage = MessageBuilder.withBody(chatMessagePayload).build();
            rabbitTemplate.send(config.getDirectMessageQueueName(), amqpMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PrivateUserException ignore) {
        }
    }
}
