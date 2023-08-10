import os
import random
import string
import sys
import threading
import time
import uuid
from datetime import datetime
from json import decoder, encoder

import msgpack
import requests
import websocket
from tinydb import TinyDB

import ChatMessage as chatM
from ChatMessage import ChatMessage, MessageType


class ChatClient:
    def __init__(self, instance: str, data_path: str):
        with open("settings.json", mode="r", encoding="utf-8") as file:
            self.config = decoder.JSONDecoder().decode("".join(file.readlines()))
        self.inst = instance
        os.chdir(data_path)
        os.mkdir(f"{instance}")

        self.operation_log: TinyDB = TinyDB(f"{instance}/op.json")
        self.performance_log: TinyDB = TinyDB(f"{instance}/perf.json")
        self.client: websocket.WebSocket = self.connect()

        self.recv_thread_running = True
        recv_thread = threading.Thread(target=self.client_recv)
        recv_thread.start()

    def register(self) -> None:
        data = encoder.JSONEncoder().encode(
            {
                "username": self.inst,
                "password": "123456789",
                "prv": False
            })
        self.op_log(f"Registering {self.inst}")

        req = requests.request(url=self.config["registration_endpoint"],
                               method="POST",
                               data=data,
                               headers={"Content-Type": "application/json"})
        self.op_log(f"Registration status: {req.status_code}")
        if req.status_code not in (200, 409):
            sys.exit(1)
        time.sleep(1)

    def get_uuid(self) -> uuid.UUID:
        self.op_log(f"Authenticating...")
        data = encoder.JSONEncoder().encode({
            "username": self.inst,
            "password": "123456789"
        })
        req = requests.request(url=self.config["authentication_endpoint"],
                               method="post",
                               data=data,
                               headers={"Content-Type": "application/json",
                                        "Content-Length": f"{str(len(data))}"})
        if req.status_code != 200:
            self.op_log(f"Authentication code: {req.status_code}")
            sys.exit(1)
        resolved_uuid = uuid.UUID(msgpack.unpackb(req.content))
        self.op_log(f"Resolved: {resolved_uuid}")
        return resolved_uuid

    def connect(self):
        self.register()
        user_id: uuid.UUID = self.get_uuid()
        header = {
            "X-User-Id": user_id.__str__()
        }
        self.op_log("Establishing stomp connection over websocket and setting up subscription...")
        client = websocket.WebSocket()
        client.connect(url=self.config["router_endpoint"], header=header)
        self.op_log("Connection successful.")
        return client

    def client_recv(self):
        counter = 0
        while counter < self.config["client_count"] * self.config["message_count"]:
            message = self.client.recv()
            self.received_message_log(message)
            counter += 1
        self.recv_thread_running = False

    def send_messages(self):
        message = ChatMessage(
            type=MessageType.CHAT,
            sender=self.inst,
            destination=None,
            time=None,
            content=None
        )

        with open("instances.json", mode="r", encoding="utf-8") as file:
            message_targets = decoder.JSONDecoder().decode("".join(file.readlines()))
        for target in message_targets:
            for i in range(self.config["message_count"]):
                self.op_log(f"Sending message #{i} to target: {target}...")
                message.destination = target
                message.content = generate_random_string(self.config["message_length"])
                message.time = int(time.time())
                msg_body = chatM.serialize_chat_message(message)
                self.client.send_binary(msg_body)
                self.op_log(f"Sent!")
                time.sleep(.5)
        self.op_log("All messages sent!")
        self.close()

    def op_log(self, log: str):
        timestamp = time.time()
        formatted_time = datetime.fromtimestamp(timestamp).strftime('%H:%M:%S.%f')
        entry = {
            "timestamp": formatted_time,
            "event": log
        }
        self.operation_log.insert(entry)

    def received_message_log(self, message):
        if type(message) is not bytes:
            self.op_log(f"Message is not bytes! It was instead: {type(message)}. Body: {message}")
            self.client.close()
            return
        message = chatM.deserialize_chat_message(message)
        timestamp = int(time.time())
        entry = {
            "received": timestamp,
            "transmitted": int(message.time.timestamp()),
            "from": message.sender,
            "body": str(message.content.encode("utf-8"))
        }
        self.performance_log.insert(entry)

    def close(self):
        while self.recv_thread_running:
            time.sleep(5)
        self.client.close()
    pass


def generate_random_string(length):
    characters = string.ascii_letters + string.digits  # Includes both letters (lowercase and uppercase) and digits
    random_string = ''.join(random.choice(characters) for _ in range(length))
    return random_string
