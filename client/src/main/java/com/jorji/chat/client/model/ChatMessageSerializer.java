package com.jorji.chat.client.model;

import com.jorji.chat.client.model.ChatMessage;
import com.jorji.chat.client.model.MessageType;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.time.Instant;

public class ChatMessageSerializer {

    public static byte[] serialize(ChatMessage chatMessage) {
        try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
            packer.packInt(chatMessage.getType().ordinal())
                    .packString(chatMessage.getContent())
                    .packString(chatMessage.getSender())
                    .packString(chatMessage.getDestination())
                    .packLong(chatMessage.getTime().getEpochSecond());
            return packer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ChatMessage deserialize(byte[] bytes) {
        try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(bytes)) {
            MessageType messageType = MessageType.values()[unpacker.unpackInt()];
            String content = unpacker.unpackString();
            String sender = unpacker.unpackString();
            String destination = unpacker.unpackString();
            long epochSecond = unpacker.unpackLong();
            Instant time = Instant.ofEpochSecond(epochSecond);

            return new ChatMessage(messageType, content, sender, destination, time);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
