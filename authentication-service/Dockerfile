# Use a GraalVM base image to build the native image
FROM ghcr.io/graalvm/graalvm-community:20.0.2-ol9-20230725 AS builder

WORKDIR /app
COPY . .

# Build the native image using GraalVM
RUN ./mvnw native:compile -Pnative


# Create the final image with Oracle Linux 9 slim
FROM oraclelinux:9-slim

# Copy the compiled executable from the builder stage to the final image
COPY --from=builder /app/target/authentication-service /usr/bin/authentication-service

# Set the entrypoint to execute the compiled executable
ENTRYPOINT ["authentication-service"]
