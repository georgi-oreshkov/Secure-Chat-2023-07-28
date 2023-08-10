package com.jorji.chatutil.model;

import lombok.Getter;

@Getter
public enum MessageType {

    CHAT((short) 0),
    CHAT_GROUP((short) 1),
    CHAT_PRIVATE((short) 2);



    private final short code;

    MessageType(short code) {
        this.code = code;
    }

    public static MessageType fromCode(short code) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.code == code) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Invalid message code: " + code);
    }

}
