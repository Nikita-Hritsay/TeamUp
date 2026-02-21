# Message Service

## Overview

Spring Cloud Stream application that consumes user-related messages (e.g. from Kafka or RabbitMQ), runs simple “email” and “sms” style functions (currently logging), and can be extended for real notifications. It is part of the Docker Compose stack but does not register with Eureka or sit behind the API Gateway.

## Spring / Java Versions

- **Spring Boot**: 4.0.1
- **Spring Cloud**: 2025.1.0
- **Java**: 17

## Main Dependencies

- `spring-cloud-stream`
- `spring-cloud-stream-binder-kafka`
- `spring-boot-starter-test` (+ test binder for tests)

## Docker / Image

- **Build**: Jib. Image: `mykyta2/message:s1` (must match `docker-compose`).
- **Build command**:
  ```bash
  mvn -f message/pom.xml clean compile jib:build
  ```

## Configuration

- **Docker Compose**: Sets `SPRING_RABBITMQ_HOST: "rabbit"`. For Kafka you would set the appropriate `spring.cloud.stream.kafka.binder.brokers` (or binder config) in config server or environment.
- The service uses Spring Cloud Stream function beans: `email` (Function&lt;UserMessageDto, UserMessageDto&gt;) and `sms` (Function&lt;UserMessageDto, Long&gt;). Bindings and topics are configured via Spring Cloud Stream properties (e.g. in Config Server or env).

## Security

No HTTP API or Spring Security in this service; it only consumes messages. No JWT or gateway in front. Access control is via network (Docker network) and broker configuration.

## Steps to Run

### Local

1. Run Kafka (or RabbitMQ if binder is switched) and ensure bootstrap config points to it.
2. Run the app:
   ```bash
   mvn -f message/pom.xml spring-boot:run
   ```
3. Default port (if any) is typically 9010 (see Compose healthcheck).

### With Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

The `messages` service depends on `rabbit` (RabbitMQ). If the app is actually using the Kafka binder, add Kafka to Compose and set the broker URL accordingly.

## General Flow

1. Message arrives on the configured input binding (e.g. from Users or another producer).
2. `UserMessageDto` is processed by the `email` and/or `sms` functions (currently console logging).
3. Health: `GET /actuator/health/readiness` (used by Compose).

## OpenAPI

No REST API or OpenAPI/Swagger; this is a stream consumer only.
