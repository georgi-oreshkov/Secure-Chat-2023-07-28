from datetime import datetime
from enum import Enum

import msgpack


class MessageType(Enum):
    CHAT = 0
    CHAT_GROUP = 1
    CHAT_PRIVATE = 2


message_type_map = {
    "CHAT": MessageType.CHAT,
    "CHAT_GROUP": MessageType.CHAT_GROUP,
    "CHAT_PRIVATE": MessageType.CHAT_PRIVATE
}


class ChatMessage:
    def __init__(self, type, content, sender, destination, time):
        self.type = type
        self.content = content
        self.sender = sender
        self.destination = destination
        self.time = time


def serialize_chat_message(chat_message):
    data = {
        'type': chat_message.type.value,
        'content': chat_message.content,
        'sender': chat_message.sender,
        'destination': chat_message.destination,
        'time': chat_message.time
    }
    return msgpack.packb(data)


def deserialize_chat_message(data):
    unpacked_data = msgpack.unpackb(data, raw=False)
    message_type = message_type_map.get(unpacked_data['type'])
    content = unpacked_data['content']
    sender = unpacked_data['sender']
    destination = unpacked_data['destination']
    time = datetime.fromtimestamp(unpacked_data['time'])

    return ChatMessage(message_type, content, sender, destination, time)
