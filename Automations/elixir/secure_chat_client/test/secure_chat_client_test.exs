defmodule SecureChatClientTest do
  use ExUnit.Case
  doctest SecureChatClient

  test "greets the world" do
    assert SecureChatClient.hello() == :world
  end
end
