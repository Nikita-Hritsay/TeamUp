# Gateway Server

## Overview

Spring Cloud Gateway is the single entry point for client traffic. It routes `/USERS/**` and `/TEAMS/**` to the Users and Teams services via Eureka, rewrites paths, validates JWT tokens (Keycloak), and applies CORS and circuit breakers. No authentication is performed by the gateway itself—Keycloak issues JWTs; the gateway only validates them.

## Spring / Java Versions

- **Spring Boot**: 3.2.3
- **Spring Cloud**: 2023.0.0
- **Java**: 17

## Main Dependencies

- `spring-cloud-starter-gateway`
- `spring-cloud-starter-netflix-eureka-client`
- `spring-cloud-starter-config`
- `spring-cloud-starter-circuitbreaker-reactor-resilience4j`
- `spring-boot-starter-security`
- `spring-security-oauth2-resource-server`
- `spring-security-oauth2-jose`
- `spring-boot-starter-actuator`

## Docker / Image

- **Build**: Jib. Image: `mykyta2/gateway-server:s1`.
- **Build command**:
  ```bash
  mvn -f gateway-server/pom.xml clean compile jib:build
  ```

## Security Configuration and Flows

- **Role**: OAuth2 Resource Server. The gateway does **not** issue tokens; it validates JWTs using Keycloak’s JWK Set.
- **Config** (`application.yml` and Docker):
  - `spring.security.oauth2.resourceserver.jwt.jwk-set-uri`: Keycloak realm JWK Set URL, e.g.  
    `http://localhost:7080/realms/TeamUp/protocol/openid-connect/certs`  
    In Docker, Compose sets:  
    `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWT-SET-URI: "http://localhost:8080/realms/TeamUp/protocol/openid-connect/certs"`  
    (Ensure this host/port matches how the gateway reaches Keycloak inside the network, e.g. `http://keycloak:8080/...`.)
- **SecurityConfig** (Reactive):
  - **Protected**: `/USERS/**`, `/TEAMS/**` require `authenticated()` (valid JWT).
  - **CORS**: Allowed origin `http://localhost:5173`, methods GET/POST/PUT/DELETE, credentials allowed.
  - **CSRF**: Disabled (stateless API).
- **Flow**: Client (e.g. TeamUp UI) obtains access token via Keycloak (Authorization Code + PKCE), sends `Authorization: Bearer <token>` on each request; gateway validates JWT and forwards to downstream services (which may remain unaware of auth).

## Routes (General Flow)

| Path       | Downstream   | Rewrite              | Filters |
|-----------|--------------|----------------------|--------|
| `/USERS/**` | Eureka `USERS` | `/USERS/(?<segment>.*)` → `/${segment}` | Circuit breaker `usersCircuitBreaker`, response header `X-Response-Time` |
| `/TEAMS/**` | Eureka `TEAMS` | `/TEAMS/(?<segment>.*)` → `/${segment}` | Circuit breaker `teamsCircuitBreaker`, response header `X-Response-Time` |

- Discovery: `spring.cloud.gateway.discovery.locator.enabled: true`, routes use `lb://USERS` and `lb://TEAMS`.
- Resilience: Resilience4j circuit breaker config in `application.yml` (e.g. `teamsCircuitBreaker`).

## OpenAPI

The gateway does not serve OpenAPI/Swagger. API documentation is provided by the downstream services (Users, Teams) at their own bases (e.g. `/swagger-ui.html` when accessed directly or via gateway path).

## Steps to Run

### Prerequisites

- Config Server running (8071).
- Eureka Server running (8070).
- Keycloak running (e.g. 7080) with realm `TeamUp` and JWK endpoint reachable at the URI configured in `jwk-set-uri`.
- Users and Teams services registered in Eureka.

### Local

```bash
mvn -f gateway-server/pom.xml spring-boot:run
```

Gateway: **http://localhost:8072**. Example: `GET http://localhost:8072/USERS/api/v1/...` with valid Bearer token.

### Docker Compose

```bash
docker compose -f docker-compose/default/docker-compose.yml up -d
```

Gateway starts after Users and Teams are healthy. Port **8072**.

## Access Points

- **API entry**: http://localhost:8072  
- **Actuator**: Included endpoints exposed as per `management.endpoints.web.exposure.include: "*"` (e.g. health, gateway routes). Restrict in production.
