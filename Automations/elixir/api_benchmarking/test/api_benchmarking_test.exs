defmodule ApiBenchmarkingTest do
  use ExUnit.Case
  doctest ApiBenchmarking

  test "greets the world" do
    assert ApiBenchmarking.hello() == :world
  end
end
