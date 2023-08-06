defmodule ConcurrentPostRequests do
  import HTTPoison

  # Function to make a single POST request
  defp make_post_request(url, headers, body) do
    case HTTPoison.post(url, body, headers) do
      {:ok, %{status_code: status}} ->
        IO.puts("Request to #{url} succeeded with status: #{status}")
      {:error, reason} ->
        IO.puts("Request to #{url} failed with reason: #{inspect reason}")
    end
  end

  # Function to make multiple concurrent POST requests
  def make_concurrent_post_requests(url, headers, body, num_requests) do
    Enum.each(1..num_requests, fn _ ->
      Task.async(fn -> make_post_request(url, headers, body) end)
    end)
    |> Enum.map(&Task.await(&1, 30_000))
  end
end
