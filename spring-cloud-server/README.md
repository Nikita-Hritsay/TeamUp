# Spring Cloud Config Server

## Overview

Centralized configuration server for TeamUp microservices. Uses **native** profile with configs on the classpath (`classpath:/configs`). No Git backend in the default setup. All services that extend `microservice-configserver-config` in Docker get their bootstrap from this server.

## Spring / Java Versions

- **Spring Boot**: 3.5.0
- **Spring Cloud**: 2025.0.0
- **Java**: 21

## Main Dependencies

- `spring-cloud-config-server`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`

## Docker / Image

- **Build**: Jib. Image: `mykyta2/config-server:s1`.
- **Build command**:
  ```bash
  mvn -f spring-cloud-server/pom.xml clean compile jib:build
  ```

## Configuration

- **Server** (`application.yml`):
  - Port: **8071**
  - `spring.cloud.config.server.native.search-locations: "classpath:/configs"`
  - Profile: `native` (no Git).
- **Config files** (in `src/main/resources/configs/`):
  - `gatewayserver.yml`, `eurekaserver.yml`
  - `users.yml`, `users-qa.yml`, `users-prod.yml`
  - `teams.yaml`, `teams-qa.yml`, `teams-prod.yml`

Clients request config via: `GET http://config-server:8071/{application}/{profile}` (e.g. `users/default`).

## Security

- No authentication is configured in the provided setup. Config can contain sensitive data; in production use HTTPS, restrict network access, and consider Spring Security or a secret manager. No JWT or Keycloak in this service.

## Steps to Run

### Local

```bash
mvn -f spring-cloud-server/pom.xml spring-boot:run
```

Config Server: **http://localhost:8071**.  
Example: `GET http://localhost:8071/users/default` (returns properties for `users` with profile `default`).

### Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

Config Server starts after `usersdb` and `teamsdb` are healthy. Other services use `SPRING_CONFIG_IMPORT=configserver:http://config-server:8071/`.

## General Flow

1. Config Server loads YAML from `classpath:/configs` by application name and profile.
2. Clients bootstrap with `spring.config.import=configserver:http://config-server:8071/` (or optional for local dev).
3. Eureka, Gateway, Users, Teams (and optionally Message) get DB URLs, Eureka URL, build versions, etc., from this server.
4. Health: `GET /actuator/health/readiness` and `/actuator/health/liveness` (used by Compose).

## OpenAPI

Config Server exposes no REST API docs; it serves configuration over HTTP. No OpenAPI/Swagger in this module.

## Client Configuration (reference)

Services that use Config Server typically set:

```yaml
spring:
  config:
    import: "optional:configserver:http://localhost:8071/"
  application:
    name: <service-name>
```

Docker Compose sets `SPRING_CONFIG_IMPORT=configserver:http://config-server:8071/` for applicable services.
