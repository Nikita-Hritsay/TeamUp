# Users Service

## Overview

The Users Service is responsible for managing user accounts, authentication, and user-related operations in the microservices system. It provides core functionality for user management and serves as the authentication provider for other services.

## Main Features

- User registration and account management
- Authentication and authorization
- User profile management
- Password reset functionality
- Role-based access control
- User preferences management

## API Endpoints

The service exposes the following main endpoints:

```
POST   /api/v1/users/register     # Register new user
POST   /api/v1/users/login        # User authentication
GET    /api/v1/users/{id}         # Get user details
PUT    /api/v1/users/{id}         # Update user information
DELETE /api/v1/users/{id}         # Delete user account
```

## Configuration

The service uses Spring Cloud Config Server for its configuration. Local configurations can be found in:

### application.yml
```yaml
spring:
  application:
    name: users
  datasource:
    url: jdbc:postgresql://localhost:5432/users_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8080

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8070/eureka/
```

### application-docker.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://users-db:5432/users_db
    
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
- Spring Security
- Netflix Eureka Client

## Database

The service uses PostgreSQL for data persistence. The database schema is managed through JPA entities and Flyway migrations.

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based authorization
- API endpoint security

## Docker Support

The service is containerized and can be run using Docker. Environment variables that can be configured:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token generation
- `SPRING_PROFILES_ACTIVE`: Active Spring profile

## Service Dependencies

- Requires Spring Cloud Config Server for configuration
- Registers with Eureka Server for service discovery
- Connects to PostgreSQL database

## Monitoring

Health and metrics endpoints are available through Spring Boot Actuator:
```
/actuator/health    # Health check endpoint
/actuator/metrics   # Metrics endpoint
/actuator/info      # Service information
``` 