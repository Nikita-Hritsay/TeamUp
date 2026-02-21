# Microservices System with Spring Boot

## Module documentation

Each module has its own README with details on versions, Docker, security, OpenAPI, and how to run:

| Module | README | Description |
|--------|--------|--------------|
| **Front (TeamUp UI)** | [front/teamup-ui/README.md](front/teamup-ui/README.md) | React + Vite SPA; Keycloak Auth Code + PKCE |
| **Eureka Server** | [eureka-server/README.md](eureka-server/README.md) | Service discovery |
| **Config Server** | [spring-cloud-server/README.md](spring-cloud-server/README.md) | Centralized configuration (native) |
| **Gateway Server** | [gateway-server/README.md](gateway-server/README.md) | API gateway, JWT validation, routes |
| **Users Service** | [users/README.md](users/README.md) | User CRUD, MySQL, OpenAPI |
| **Teams Service** | [teams/README.md](teams/README.md) | Teams & cards, MySQL, OpenAPI codegen |
| **Message Service** | [message/README.md](message/README.md) | Spring Cloud Stream (Kafka/Rabbit) consumer |

---

## Keycloak

Authentication and token issuance are handled by **Keycloak**. The Gateway validates JWTs; it does not issue tokens.

- **Keycloak (identity & flows)**: See **[docs/KEYCLOAK.md](docs/KEYCLOAK.md)** for:
  - **Authorization Code + PKCE** — used by the TeamUp UI (browser, no client secret)
  - **Client Credentials** — for server-to-server or scripts (client id + secret)
  - Realm and client setup (public client for UI, confidential for client creds)
  - Endpoints (auth, token, logout, JWK Set) and Gateway integration

Quick setup reminder:
- Create realm **TeamUp**
- Public client (e.g. `teamup-public`) with **Standard Flow** and redirect URI `http://localhost:5173/callback` for the UI
- Optional: confidential client with **Client authentication** and **Service accounts** for client credentials
- OIDC discovery: http://localhost:7080/realms/TeamUp/.well-known/openid-configuration

Postman collections (if any) are in the Config module resources folder.

---

## High-Level Architecture

The system consists of:

• **Business microservices** (Users, Teams)
• **Infrastructure services** (Config Server, Eureka, Gateway)
• **Observability stack** (Loki, Grafana, Alloy, MinIO)
• **Databases** (MySQL for Users and Teams)

Core architectural patterns:

• Spring Cloud Config – centralized configuration
• Eureka – service discovery
• API Gateway – single entry point
• Docker & Docker Compose – orchestration
• Loki + Grafana – logs & observability

---

## Services Overview (Short)

### Application Layer

• **Users Service** – user domain logic, MySQL-backed
• **Teams Service** – team domain logic, MySQL-backed
• **Gateway Server** – routes external traffic to services

### Infrastructure

• **Config Server** – provides configuration to all services
• **Eureka Server** – service registry and discovery

### Observability

• **Loki** – log aggregation (read/write/backend split)
• **Grafana** – log visualization and dashboards
• **Alloy** – log collection from Docker containers
• **MinIO** – object storage for Loki

No Cards service exists in this system.

---

## Build & Image Creation

All Spring Boot services are built locally and packaged into Docker images using **Jib**.

### Standard Build Flow

For each microservice:

```bash
mvn clean compile jib:build
```

Notes:
• `compile` is sufficient (no fat JAR needed)
• Jib builds images directly without Dockerfiles
• Image names must match those used in `docker-compose.yml`

---

## Database Setup (Manual Option)

If you want to start MySQL containers manually instead of Docker Compose:

### Users Database

```bash
docker run -d \
  -p 3306:3306 \
  --name usersdb \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=userdb \
  mysql
```

### Teams Database

```bash
docker run -d \
  -p 3308:3306 \
  --name teamsdb \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=teamsdb \
  mysql
```

### Keycloak
```bash
 docker run -d -p 127.0.0.1:7080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.5.0 start-dev
```


Docker Compose already includes these databases, so manual creation is optional.

---

## Running the System

From the repo root, using the default Compose file:

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

Docker Compose will:

• Start databases
• Start Config Server
• Start Eureka Server
• Start application services
• Start observability stack
• Wire all services into a shared network

---

## Access Points

• **Gateway (API entry)**: [http://localhost:8072](http://localhost:8072)
• **Config Server**: [http://localhost:8071](http://localhost:8071)
• **Eureka Dashboard**: [http://localhost:8070](http://localhost:8070)
• **Grafana**: [http://localhost:3000](http://localhost:3000)
• **Loki (via Gateway)**: [http://localhost:3100](http://localhost:3100)

Grafana is preconfigured with Loki as a datasource.

---

## Observability (Short)

• Logs from all containers are collected by Alloy
• Logs are stored in Loki (MinIO-backed)
• Grafana is used for log exploration and dashboards

No metrics or tracing are described here intentionally.

---

## Notes

• All services rely on Config Server at startup
• Eureka must be healthy before services register
• Healthchecks control startup order
• Networking is isolated via a single Docker bridge network

This README describes **how the system is built and run**, not internal business logic.
