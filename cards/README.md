# Cards Service

## Overview

The Cards Service manages card-related operations within the microservices system. It provides functionality for creating, managing, and organizing cards, which can be associated with teams and users. The service implements card workflow management and supports various card types and states.

## Main Features

- Card creation and management
- Card status tracking
- Card assignment to team members
- Card categorization and labeling
- Card workflow automation
- Integration with Teams Service
- Card history and activity logging

## API Endpoints

The service exposes the following main endpoints:

```
POST   /api/v1/cards              # Create new card
GET    /api/v1/cards              # List all cards
GET    /api/v1/cards/{id}         # Get card details
PUT    /api/v1/cards/{id}         # Update card information
DELETE /api/v1/cards/{id}         # Delete card

GET    /api/v1/cards/team/{teamId} # Get team's cards
POST   /api/v1/cards/{id}/assign   # Assign card to user
PUT    /api/v1/cards/{id}/status   # Update card status
GET    /api/v1/cards/{id}/history  # Get card history
```

## Configuration

The service uses Spring Cloud Config Server for its configuration. Local configurations can be found in:

### application.yml
```yaml
spring:
  application:
    name: cards
  datasource:
    url: jdbc:postgresql://localhost:5432/cards_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8082

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8070/eureka/
```

### application-docker.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://cards-db:5432/cards_db
    
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
- Spring Cloud OpenFeign
- Spring HATEOAS

## Database

The service uses PostgreSQL for data persistence with the following main entities:
- Card
- CardStatus
- CardHistory
- CardLabel
- CardAssignment

## Service Integration

- Integrates with Teams Service for team validation
- Integrates with Users Service for user information
- Uses OpenFeign clients for inter-service communication
- Implements circuit breakers for resilient service calls

## Card Workflow

Cards can have the following statuses:
- TODO
- IN_PROGRESS
- REVIEW
- DONE
- ARCHIVED

## Docker Support

The service is containerized and can be run using Docker. Environment variables that can be configured:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `SPRING_PROFILES_ACTIVE`: Active Spring profile

## Service Dependencies

- Requires Spring Cloud Config Server for configuration
- Registers with Eureka Server for service discovery
- Connects to PostgreSQL database
- Depends on Teams Service for team information
- Depends on Users Service for user information

## Monitoring

Health and metrics endpoints are available through Spring Boot Actuator:
```
/actuator/health    # Health check endpoint
/actuator/metrics   # Metrics endpoint
/actuator/info      # Service information
```

## Event Publishing

The service publishes events for:
- Card creation
- Status changes
- Assignments
- Updates
- Deletions

These events can be consumed by other services for additional processing or notifications. 