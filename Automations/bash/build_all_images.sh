#!/bin/bash

# Function to build the Docker image if Dockerfile is found
function build_docker_image {
    local directory=$1
    local dockerfile="$directory/Dockerfile"
    local image_tag="gocheto8/jorji:$directory"

    if [[ -f "$dockerfile" ]]; then
        echo "Building Docker image for directory: $directory"
        docker build --no-cache -t "$image_tag" "$directory"
#        docker push "$image_tag"
        echo "Docker image built with tag: $image_tag"

    fi
}

# Loop through all directories in the current directory and build the Docker image if Dockerfile is found
for directory in */; do
    directory=${directory%/}  # Remove trailing slash
    if [[ -d "$directory" ]]; then
        build_docker_image "$directory"
    fi
done