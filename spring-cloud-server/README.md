# Spring Cloud Config Server

## Overview

The Spring Cloud Config Server is a centralized configuration service that manages all configuration properties for the microservices in the system. It provides a reliable and scalable way to externalize configuration across multiple applications and environments.

## Key Features

- Centralized configuration management for all microservices
- Git-backed configuration storage
- Environment-specific configuration support
- Dynamic configuration updates
- Encryption/decryption of sensitive properties
- RESTful API for configuration access

## Configuration Files

The server manages the following types of configuration files:
- `application.yml`: Default configuration shared across all services
- `application-{profile}.yml`: Environment-specific configurations
- `{service-name}.yml`: Service-specific default configuration
- `{service-name}-{profile}.yml`: Service and environment-specific configuration

## Accessing Configurations

Services can access their configurations through the following URL patterns:

```
http://localhost:8071/{service-name}/{profile}
```

Example URLs:
- `http://localhost:8071/users/default`
- `http://localhost:8071/teams/docker`

## Server Configuration

The Config Server itself is configured through `application.yml`:

```yaml
server:
  port: 8071

spring:
  application:
    name: spring-cloud-server
  cloud:
    config:
      server:
        git:
          uri: ${CONFIG_REPO_URI:file:///config-repo}
          default-label: ${CONFIG_REPO_BRANCH:master}
          search-paths:
            - '*'
```

## Security

- The server can be configured with authentication
- Sensitive properties can be encrypted
- HTTPS should be enabled in production

## Client Configuration

Services connecting to the Config Server need the following configuration:

```yaml
spring:
  config:
    import: "optional:configserver:http://localhost:8071"
  cloud:
    config:
      fail-fast: true
      retry:
        max-attempts: 6
        initial-interval: 1000
        max-interval: 2000
        multiplier: 1.1
```

## Health Check

The Config Server's health can be monitored at:
```
http://localhost:8071/actuator/health
```

## Docker Support

When running in Docker, the service is configured using environment variables:
- `CONFIG_REPO_URI`: Git repository URL for configurations
- `CONFIG_REPO_BRANCH`: Git branch to use (defaults to master)
- `SPRING_PROFILES_ACTIVE`: Active profile for the server 