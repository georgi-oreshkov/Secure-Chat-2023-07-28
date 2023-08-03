package com.jorji.chat.routingservice.model;

import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Data
@Builder
public class WebSocketMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String destination;
    private Instant time;

    public byte[] toAmqpMessageBody(){
        String body = String.format("%s\u001F%s\u001F%s\u001F%s", sender, destination, time.toString(), content);
        return body.getBytes(StandardCharsets.UTF_8);
    }

    public static WebSocketMessage fromAmqpMessageBody(byte[] body){
        String[] decodedBody = new String(body).split("\u001F", 4);
        return WebSocketMessage.builder()
                .sender(decodedBody[0])
                .destination(decodedBody[1])
                .time(Instant.parse(decodedBody[2]))
                .content(decodedBody[3])
                .build();
    }

    public String getMessagePayload(){
        return time.toString() + '\u001F' + content;
    }

}
