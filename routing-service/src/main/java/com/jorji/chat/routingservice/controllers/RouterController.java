package com.jorji.chat.routingservice.controllers;

import com.jorji.chat.routingservice.model.ChatMessage;
import com.jorji.chat.routingservice.services.RouterService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class RouterController {
    private final RouterService routerService;
    @MessageMapping("/chat.send")
    public void sendPrivate(ChatMessage message){
        routerService.sendDirectMessage(message);
    }

    @MessageMapping("/group.send")
    public void sendGroup(ChatMessage message){
        routerService.sendGroupMessage(message);
    }
}
