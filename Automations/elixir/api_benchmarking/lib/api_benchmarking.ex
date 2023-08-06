defmodule ApiBenchmarking do
  import ConcurrentPostRequests

  def run do
    url = "http://localhost:9001/api/register"
    headers = %{"Authorization": "Bearer your_token", "Content-Type": "application/json"}
    body = ~s({"key": "value"})
    num_requests = 2

    make_concurrent_post_requests(url, headers, body, num_requests)
  end
end

Main.run()
