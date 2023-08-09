import json.decoder
import os
import random
import string
import sys
import time
import uuid
from datetime import datetime
from json import decoder, encoder
import threading

import msgpack
import requests
from tinydb import TinyDB

import ChatMessage as chatM
from ChatMessage import ChatMessage, MessageType
from stomp_ws.client import Client as stClient


class Client:
    def __init__(self, instance: str, data_path: str):
        with open("client_settings.json", mode="r", encoding="utf-8") as file:
            self.config = decoder.JSONDecoder().decode("".join(file.readlines()))
        self.inst = instance
        os.chdir(data_path)
        os.mkdir(f"{instance}")

        self.operation_log: TinyDB = TinyDB(f"{instance}/op.json")
        self.performance_log: TinyDB = TinyDB(f"{instance}/perf.json")

        # create the connection.
        self.client = self.connect()

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
        self.op_log(f"Resolving uuid...")
        req = requests.request(url=self.config["user_resolution_endpoint"] + self.inst, method="get")
        if req.status_code != 200:
            self.op_log(f"UUID resolution code: {req.status_code}")
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
        client = stClient(
            url=self.config["router_endpoint"],
            header=header
        )
        client.connect()
        client.subscribe("/direct", self.received_message_log)
        self.op_log("Connection successful.")
        return client

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
        self.op_log(f"Sending messages to targets: {message_targets}")
        for target in message_targets:
            for i in range(self.config["message_count"]):
                message.destination = target
                message.content = generate_random_string(self.config["message_length"])
                message.time = int(time.time() * 1000)
                msg_body = chatM.serialize_chat_message(message)
                self.client.send(destination="/message/chat.send", body=msg_body)
        self.op_log("All messages sent!")
        time.sleep(10)

    def op_log(self, log: str):
        timestamp = time.time()
        formatted_time = datetime.fromtimestamp(timestamp).strftime('%H:%M:%S.%f')
        entry = {
            "timestamp": formatted_time,
            "event": log
        }
        self.operation_log.insert(entry)

    def on_stomp_error(self, frame):
        error = f"ERROR: {frame.body.decode('utf-8')}"
        self.op_log(error)
        pass

    def received_message_log(self, frame):
        print("Message!")
        message = chatM.deserialize_chat_message(frame.body)
        timestamp = int(time.time())
        entry = {
            "received": timestamp,
            "transmitted": message.time,
            "from": message.sender,
            "body": message.content.decode("utf-8")
        }
        self.performance_log.insert(entry)

    pass


def generate_random_string(length):
    characters = string.ascii_letters + string.digits  # Includes both letters (lowercase and uppercase) and digits
    random_string = ''.join(random.choice(characters) for _ in range(length))
    return random_string
