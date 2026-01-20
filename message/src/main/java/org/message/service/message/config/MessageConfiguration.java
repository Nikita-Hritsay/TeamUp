package org.message.service.message.config;

import org.message.service.message.dto.UserMessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageConfiguration {

    @Bean
    public Function<UserMessageDTO, String> email() {
        return userMessageDTO -> {
            System.out.println("Emailing: " + userMessageDTO.email());
            return userMessageDTO.email();
        };
    }

}
