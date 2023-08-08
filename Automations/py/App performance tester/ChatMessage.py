import msgpack
from datetime import datetime
from enum import Enum


class MessageType(Enum):
    CHAT = 1
    CHAT_PRIVATE = 2


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
        'time': chat_message.time.timestamp()
    }
    return msgpack.packb(data)


def deserialize_chat_message(data):
    unpacked_data = msgpack.unpackb(data, raw=False)
    message_type = MessageType(unpacked_data['type'])
    content = unpacked_data['content']
    sender = unpacked_data['sender']
    destination = unpacked_data['destination']
    time = datetime.fromtimestamp(unpacked_data['time'])

    return ChatMessage(message_type, content, sender, destination, time)

