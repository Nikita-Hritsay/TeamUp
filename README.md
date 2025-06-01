# Microservices System with Spring Boot

This project implements a microservices-based architecture using Spring Boot, demonstrating modern cloud-native application development practices. The system consists of multiple independent services that work together to provide a comprehensive application platform.

## System Architecture

The system is built using the following technologies and patterns:
- Spring Boot for microservice implementation
- Spring Cloud Config for centralized configuration management
- Netflix Eureka for service discovery
- Docker for containerization and deployment
- Docker Compose for orchestrating the entire system

## Services Overview

### Core Services

1. **Users Service**
   - Manages user accounts and authentication
   - Handles user profile data and permissions

2. **Teams Service**
   - Manages team creation and membership
   - Handles team-related operations and configurations

3. **Cards Service**
   - Manages card-related functionality
   - Handles card operations and data persistence

### Infrastructure Services

1. **Spring Cloud Config Server** (Port: 8071)
   - Centralized configuration management
   - Provides configuration for all microservices
   - Supports profile-based configuration management

2. **Eureka Server** (Port: 8070)
   - Service discovery and registration
   - Health monitoring of registered services
   - Load balancing support

## Deployment

The entire system is containerized and can be launched using Docker Compose:

```bash
docker-compose up
```

### What Docker Compose Does

The `docker-compose.yml` configuration:
- Creates containers for each microservice
- Sets up necessary networking between services
- Manages service dependencies and startup order
- Connects services to their respective databases
- Configures environment-specific settings
- Launches infrastructure services (Config Server, Eureka)

### Service Dependencies

1. All services depend on the Spring Cloud Config Server for configuration
2. Services register themselves with Eureka Server for service discovery
3. Database containers are launched before their respective services

## Getting Started

1. Ensure Docker and Docker Compose are installed
2. Clone the repository
3. Run `docker-compose up` in the root directory
4. Services will be available at their respective ports
5. Monitor service health through Eureka dashboard at http://localhost:8070

## Configuration

- Each service has its own configuration in the Config Server
- Environment-specific configurations are managed through profiles
- Local development can use application.yml files
- Docker environment uses application-docker.yml configurations

## Service URLs

- Config Server: http://localhost:8071
- Eureka Server: http://localhost:8070
- Users Service: http://localhost:8080
- Teams Service: http://localhost:8081
- Cards Service: http://localhost:8082

For detailed information about each service, please refer to their individual README files in their respective directories. 