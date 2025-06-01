# Teams Service

## Overview

The Teams Service manages team-related operations and team membership in the microservices system. It provides functionality for creating and managing teams, handling team memberships, and managing team-specific configurations.

## Main Features

- Team creation and management
- Team membership operations
- Team roles and permissions
- Team settings and preferences
- Team collaboration features
- Integration with Users Service for member management

## API Endpoints

The service exposes the following main endpoints:

```
POST   /api/v1/teams              # Create new team
GET    /api/v1/teams              # List all teams
GET    /api/v1/teams/{id}         # Get team details
PUT    /api/v1/teams/{id}         # Update team information
DELETE /api/v1/teams/{id}         # Delete team

POST   /api/v1/teams/{id}/members # Add team member
DELETE /api/v1/teams/{id}/members/{userId} # Remove team member
GET    /api/v1/teams/{id}/members # List team members
```

## Configuration

The service uses Spring Cloud Config Server for its configuration. Local configurations can be found in:

### application.yml
```yaml
spring:
  application:
    name: teams
  datasource:
    url: jdbc:postgresql://localhost:5432/teams_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8081

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8070/eureka/
```

### application-docker.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://teams-db:5432/teams_db
    
eureka:
  client:
    serviceUrl:
      defaultZone: http://eureka-server:8070/eureka/
```

## Dependencies

- Spring Boot
- Spring Cloud Config Client
- Spring Data JPA
- PostgreSQL
- Netflix Eureka Client
- Spring Cloud OpenFeign (for service communication)

## Database

The service uses PostgreSQL for data persistence with the following main entities:
- Team
- TeamMember
- TeamRole
- TeamSettings

## Service Integration

- Integrates with Users Service for member validation and user information
- Uses OpenFeign clients for inter-service communication
- Implements circuit breakers for resilient service calls

## Docker Support

The service is containerized and can be run using Docker. Environment variables that can be configured:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `SPRING_PROFILES_ACTIVE`: Active Spring profile

## Service Dependencies

- Requires Spring Cloud Config Server for configuration
- Registers with Eureka Server for service discovery
- Connects to PostgreSQL database
- Depends on Users Service for user information

## Monitoring

Health and metrics endpoints are available through Spring Boot Actuator:
```
/actuator/health    # Health check endpoint
/actuator/metrics   # Metrics endpoint
/actuator/info      # Service information
```

## Error Handling

The service implements global error handling for:
- Invalid team operations
- Member management errors
- Database constraints
- Service communication failures 