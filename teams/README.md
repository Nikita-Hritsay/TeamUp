# Teams Service

## Overview

Teams microservice: teams, cards, and team members. Uses Spring Cloud Config, Eureka, OpenFeign, MySQL, and OpenAPI code generation from an OpenAPI spec. REST API is defined in `src/main/resources/openapi/openapi.yml`; Spring controllers implement the generated API interfaces. Authentication is enforced at the Gateway (Keycloak JWT); this service is called with a valid token.

## Spring / Java Versions

- **Spring Boot**: 3.2.3
- **Spring Cloud**: 2023.0.0
- **Java**: 21

## Main Dependencies

- `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`
- `mysql-connector-j`
- `spring-cloud-starter-config`, `spring-cloud-starter-netflix-eureka-client`, `spring-cloud-starter-openfeign`
- `springdoc-openapi-starter-webmvc-ui` (2.6.0)
- `openapi-generator-maven-plugin` (7.4.0) — generates API interfaces and models from `openapi.yml`
- `spring-boot-starter-actuator`

## Docker / Image

- **Build**: Jib. Image: `mykyta2/teams:s1`.
- **Build command**:
  ```bash
  mvn -f teams/pom.xml clean compile jib:build
  ```
  (OpenAPI generation runs in `generate-sources` phase.)

## Security

- No login or JWT issuance in this service. The API Gateway validates Keycloak JWTs for `/TEAMS/**` and forwards. This service is backend-only from the gateway.

## Configuration

- **Config Server**: `configs/teams.yaml`, `teams-qa.yml`, `teams-prod.yml`.
- **Docker**: Compose sets `SPRING_APPLICATION_NAME: teams`, `SPRING_DATASOURCE_URL: jdbc:mysql://teamsdb:3306/teamsdb`. DB credentials from `common-config.yml`.

## Database

- **MySQL**. Database: `teamsdb`. Compose: `teamsdb`, host port 3308 → 3306. Schema via JPA.

## OpenAPI

- **Spec**: `src/main/resources/openapi/openapi.yml` (OpenAPI 3.0.3). Defines Cards and Teams (and possibly other) tags and paths.
- **Code generation**: Maven plugin generates Spring interfaces and models into `target/generated-sources/openapi`; `build-helper-maven-plugin` adds that as source.
- **Runtime docs**: Springdoc serves Swagger UI. When running:
  - Direct: `http://localhost:8081/swagger-ui.html` (or port from config).
  - Via Gateway: `http://localhost:8072/TEAMS/swagger-ui.html` (path depends on gateway rewrite).

## API Endpoints (summary from spec)

- **Cards**: e.g. `GET /api/v1/cards/build-version`, `POST/GET /api/v1/cards`, and other card CRUD.
- **Teams**: Team CRUD and team members (paths as in `openapi.yml`).  
Exact operations and schemas: see `openapi.yml` or Swagger UI.

## Steps to Run

### Local

1. MySQL (`teamsdb`), Config Server (8071), Eureka (8070).
2. Configure datasource and optional `SPRING_CONFIG_IMPORT`.
3. Run:
   ```bash
   mvn -f teams/pom.xml spring-boot:run
   ```
4. Default port 8081. Health: `GET /actuator/health/readiness`.

### Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

`teams` starts after Config Server and Eureka; uses `teamsdb`.

## General Flow

1. Config Server provides `teams` (and profile) configuration.
2. Service registers with Eureka (e.g. as `TEAMS`).
3. Gateway routes `/TEAMS/**` to this service with path rewrite to `/**`.
4. Client sends `Authorization: Bearer <Keycloak JWT>`; gateway validates and forwards.
5. Controllers implement generated API interfaces; persistence via JPA.

## Monitoring

- **Actuator**: Health (readiness/liveness), metrics. Compose healthcheck uses `/actuator/health/readiness`.
