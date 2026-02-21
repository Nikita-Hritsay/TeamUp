# Eureka Server

## Overview

Netflix Eureka service registry for TeamUp. All microservices (Users, Teams, Gateway) register here and the Gateway uses Eureka to discover and route to them. Config is loaded from Spring Cloud Config Server at startup.

## Spring / Java Versions

- **Spring Boot**: 3.2.3
- **Spring Cloud**: 2023.0.0
- **Java**: 17

## Main Dependencies

- `spring-cloud-starter-netflix-eureka-server`
- `spring-cloud-starter-config`
- `spring-boot-starter-actuator`

## Docker / Image

- **Build**: Jib (no Dockerfile). Image name must match `docker-compose`: `mykyta2/eureka-server:s1`.
- **Build command** (from repo root or `eureka-server/`):
  ```bash
  mvn -f eureka-server/pom.xml clean compile jib:build
  ```

## Security

Eureka Server in this setup has no custom security (no Spring Security). It is intended for internal use on the Docker network. Exposed port is 8070; restrict access in production if needed.

## Configuration

- **Local**: `application.yml` sets `spring.application.name: eurekaserver` and optional Config Server import.
- **Config Server**: `eurekaserver.yml` in Config Server’s native search path can override properties.
- **Docker**: Compose sets `SPRING_CONFIG_IMPORT=configserver:http://config-server:8071/` and Eureka client URL for services that extend `microservice-configserver-config` / `microservice-eureka-config`.

## Steps to Run

### Local (Config Server must be up)

1. Start Config Server (e.g. on 8071).
2. From project root:
   ```bash
   mvn -f eureka-server/pom.xml spring-boot:run
   ```
3. Dashboard: **http://localhost:8070**

### With Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

Eureka starts after Config Server and DBs are healthy. Dashboard: **http://localhost:8070**.

## General Flow

1. Config Server provides bootstrap config (including optional Config Server URL).
2. Eureka starts and (if configured) may register itself; other services use `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eurekaserver:8070/eureka/`.
3. Gateway and business services register with Eureka and are discovered by service id (e.g. `USERS`, `TEAMS`).
4. Health: `GET /actuator/health/readiness` and `/actuator/health/liveness` (used by Compose healthchecks).

## OpenAPI

Eureka does not expose REST API docs for application endpoints; it exposes the Eureka dashboard and REST API for registration/discovery. No OpenAPI/Swagger in this module.
