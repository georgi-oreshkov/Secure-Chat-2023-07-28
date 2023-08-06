import requests
import threading
import random
import string


def generate_random_string(length):
    characters = string.ascii_letters + string.digits
    return ''.join(random.choice(characters) for _ in range(length))


def send_post_request(url, data):
    headers = {
        "Content-Type": "application/json"
    }
    try:
        response = requests.post(url, json=data, headers=headers)
        print(f"Status Code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Error: {e}")


def main():
    url = "http://localhost:9001/api/register"  # Replace this with the target URL

    num_requests = 1000  # Change this to the desired number of requests

    # Create multiple threads to send POST requests concurrently
    threads = []
    for _ in range(num_requests):
        data = {
            "username": generate_random_string(10),
            "password": "1234567890",
            "prv": random.randint(1, 3) > 1
        }
        threads.append(threading.Thread(target=send_post_request, args=(url, data)))

    # Start the threads
    for thread in threads:
        thread.start()

    # Wait for all threads to complete
    for thread in threads:
        thread.join()


if __name__ == "__main__":
    main()
