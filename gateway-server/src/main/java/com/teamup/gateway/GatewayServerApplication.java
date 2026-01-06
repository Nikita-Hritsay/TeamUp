package com.teamup.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class GatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator getRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/USERS/**")
                        .filters(f -> f.rewritePath("/USERS/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString())
                                .circuitBreaker(config -> config.setName("usersCircuitBreaker")))
                        .uri("lb://USERS"))
                .route(p -> p.path("/TEAMS/**")
                        .filters(f -> f.rewritePath("/TEAMS/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", new Date().toString())
                                .circuitBreaker(config -> config.setName("teamsCircuitBreaker")))
                        .uri("lb://TEAMS"))
                .build();
    }
} 