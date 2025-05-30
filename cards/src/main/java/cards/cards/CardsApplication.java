package cards.cards;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info = @Info(
        title = "Cards microservice REST API Docs",
        description = "TeamUp Cards microservice",
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
@EnableDiscoveryClient
public class CardsApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CardsApplication.class, args);
    }

}
