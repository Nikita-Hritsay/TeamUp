# Microservices System with Spring Boot


How to create users:
* Open Keycloak
* Create Realm TeamUp
* added Client with  Client Authentication On and Service Account On (this will be for Client Creds scope)
* also create one for Users as Standard Flow - this one is for Users
* Postman json is in the Config module - resources folder;

Keycloak admin panel - http://localhost:7080/realms/TeamUp/.well-known/openid-configuration
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

To start everything:

```bash
docker compose up -d
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
