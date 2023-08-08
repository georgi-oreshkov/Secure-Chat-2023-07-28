package com.jorji.chat.routingservice.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String destination;
    private Instant time;
}
