import os
from datetime import datetime
from json import decoder, encoder
import multiprocessing
from ChatClient import Client

if __name__ == "__main__":
    def worker(task_id, path):
        processor = Client(task_id, path)
        processor.send_messages()


    with open("main_setting.json", mode="r", encoding="utf-8") as file:
        config = decoder.JSONDecoder().decode("".join(file.readlines()))

    formatted_time = datetime.now().strftime('%Y-%m-%d_%H:%M:%S')
    os.mkdir(f"data/Run{formatted_time}")

    num_tasks = config["client-count"]
    instances = []

    with open(f"./data/Run{formatted_time}/instances.json", mode="w", encoding="utf-8") as file:
        instance_ids = []
        for i in range(num_tasks):
            instance_id = f"py{i}"
            instance = multiprocessing.Process(target=worker, args=(instance_id, f"data/Run{formatted_time}"))
            instance_ids.append(instance_id)
            instances.append(instance)
            file.write(encoder.JSONEncoder().encode(instance_ids))

    for instance in instances:
        instance.start()

    for instance in instances:
        instance.join()

    print("All tasks completed")
