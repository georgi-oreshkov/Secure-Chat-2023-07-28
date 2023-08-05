FROM ghcr.io/graalvm/graalvm-community:20.0.2-ol9-20230725 AS builder
WORKDIR /app
COPY . .
RUN ./mvnw native:compile -Pnative

FROM oraclelinux:9-slim
COPY --from=builder /app/target/user-management-service /usr/bin/user-management-service
ENTRYPOINT ["user-management-service"]
