package org.users.users;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(
        title = "Users microservice REST API Docs",
        description = "TeamUp Users microservice",
        version = "v1",
        contact = @Contact(
                name = "Mykyta Hrytsai",
                email = "hritsaynikita@gmail.com",
                url = "www.google.com"
        ),
        license = @License(
                name = "Apache 2.0",
                url = "www.google.com"
        )
    )
)
@EnableFeignClients
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

}
