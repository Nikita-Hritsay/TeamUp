# Users Service

## Overview

Users microservice: user CRUD, profiles, and integration with Teams (e.g. team members). It uses Spring Cloud Config, Eureka, OpenFeign, and Spring Cloud Stream (Kafka binder). REST API is documented with Springdoc OpenAPI. Authentication is handled by Keycloak at the Gateway; this service is called with a valid JWT and does not perform login itself.

## Spring / Java Versions

- **Spring Boot**: 3.2.3
- **Spring Cloud**: 2023.0.0
- **Java**: 21

## Main Dependencies

- `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`
- `mysql-connector-j`
- `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client`, `spring-cloud-starter-openfeign`
- `spring-cloud-stream`, `spring-cloud-stream-binder-kafka`
- `springdoc-openapi-starter-webmvc-ui` (2.3.0)
- `spring-boot-starter-actuator`

## Docker / Image

- **Build**: Jib. Image: `mykyta2/users:s1` (must match `docker-compose`).
- **Build command**:
  ```bash
  mvn -f users/pom.xml clean compile jib:build
  ```

## Security

- This service does **not** implement login or JWT issuance. The API Gateway validates Keycloak JWTs and forwards requests. Users service can trust the gateway or optionally validate JWT again; by default it is called after gateway auth.
- No Spring Security configuration is required in this module for the gateway-to-service flow; the gateway enforces authentication for `/USERS/**`.

## Configuration

- **Config Server**: Service-specific config in `configs/users.yml` (e.g. `build.version`). Profiles: `users-qa.yml`, `users-prod.yml`.
- **Local**: Bootstrap uses optional `configserver:http://localhost:8071/`. Datasource and Eureka URL come from config or environment.
- **Docker**: Compose sets `SPRING_APPLICATION_NAME: users`, `SPRING_DATASOURCE_URL: jdbc:mysql://usersdb:3306/userdb`, and `SPRING_RABBITMQ_HOST: rabbit`. DB credentials from `common-config.yml` (e.g. root/root).

## Database

- **MySQL**. Database: `userdb`. Schema managed by JPA (Hibernate). Compose service: `usersdb`, port 3306.

## OpenAPI Docs

- **Springdoc** is included; when the service is running, Swagger UI is typically at:
  - Direct: `http://localhost:8083/swagger-ui.html` (port 8083 as in Compose healthcheck).
  - Via Gateway: `http://localhost:8072/USERS/swagger-ui.html` (if the app is mounted at `/` and gateway rewrites `/USERS/**` to `/api/v1/...` as configured—confirm path rewrite in gateway).

## API Endpoints (summary)

- `POST /api/v1/users/register` — Register (may delegate to Keycloak or local DB depending on design).
- `POST /api/v1/users/login` — Login (if used; otherwise auth is Keycloak-only).
- `GET /api/v1/users/{id}` — Get user.
- `PUT /api/v1/users/{id}` — Update user.
- `DELETE /api/v1/users/{id}` — Delete user.  
Exact paths and request/response shapes are in the OpenAPI spec (Swagger UI).

## Steps to Run

### Local

1. MySQL running (e.g. `userdb` on 3306), Config Server (8071), Eureka (8070).
2. Set `SPRING_DATASOURCE_URL`, credentials, and optionally `SPRING_CONFIG_IMPORT=configserver:http://localhost:8071/`.
3. Run:
   ```bash
   mvn -f users/pom.xml spring-boot:run
   ```
4. Service port: 8083 (from config). Health: `GET /actuator/health/readiness`.

### Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

`users` starts after Config Server and Eureka are healthy; uses `usersdb` and `rabbit`.

## General Flow

1. Config Server provides `users` (and profile) configuration.
2. Service registers with Eureka as `USERS` (or name from config).
3. Gateway routes `GET /USERS/api/v1/...` to this service (path rewritten to `/api/v1/...`).
4. Client sends `Authorization: Bearer <Keycloak JWT>`; gateway validates and forwards; service processes request.
5. Optional: events published to Kafka/Rabbit for message service (e.g. user created).

## Monitoring

- **Actuator**: Health (readiness/liveness), metrics, info. Endpoints exposed as per config. Use `/actuator/health/readiness` for orchestration.
