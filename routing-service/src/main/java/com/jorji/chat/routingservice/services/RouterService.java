package com.jorji.chat.routingservice.services;

import com.jorji.chat.routingservice.model.WebSocketMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RouterService {
    private final RabbitTemplate rabbitTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendDirectMessage(WebSocketMessage message){
        Message amqpMessage = MessageBuilder
                .withBody(message.toAmqpMessageBody())
                .build();

        rabbitTemplate.send("user", amqpMessage);
    }

    public void sendGroupMessage(WebSocketMessage message){
        Message amqpMessage = MessageBuilder
                .withBody(message.toAmqpMessageBody())
                .build();

        rabbitTemplate.send("group", amqpMessage);
    }

    @RabbitListener(queues = "direct")
    public void receiveRabbitMessage(byte[] message){
        WebSocketMessage webSocketMessage = WebSocketMessage.fromAmqpMessageBody(message);
        messagingTemplate.convertAndSendToUser(
                webSocketMessage.getDestination(),
                "/direct/" + webSocketMessage.getSender(),
                webSocketMessage.getMessagePayload()
        );

    }
}
