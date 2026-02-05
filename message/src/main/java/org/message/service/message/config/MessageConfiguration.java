package org.message.service.message.config;

import org.message.service.message.dto.UserMessageDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageConfiguration {

    @Bean
    public Function<UserMessageDto, UserMessageDto> email() {
        return userMessageDTO -> {
            System.out.println("Emailing: " + userMessageDTO.email());
            return userMessageDTO;
        };
    }

    @Bean
    public Function<UserMessageDto, Long> sms() {
        return userMessageDTO -> {
            System.out.println("Sending message: " + userMessageDTO.mobileNumber());
            return userMessageDTO.id();
        };
    }

}
