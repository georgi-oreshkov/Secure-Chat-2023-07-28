package com.jorji.chat.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String destination;
    private Long time;

    public ChatMessage(String content, String sender, String destination) {
        this.content = content;
        this.sender = sender;
        this.destination = destination;

        this.type = MessageType.CHAT;
        this.time = Instant.now().toEpochMilli();
    }
}
