package org.message.service.message.config;

import org.junit.jupiter.api.Test;
import org.message.service.message.dto.UserMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    void emailFunctionBeanExists() {
        Function<UserMessageDto, UserMessageDto> emailFunction = context.getBean("email", Function.class);
        assertNotNull(emailFunction);
    }

    @Test
    void smsFunctionBeanExists() {
        Function<UserMessageDto, Long> smsFunction = context.getBean("sms", Function.class);
        assertNotNull(smsFunction);
    }

    @Test
    void emailFunctionReturnsSameDto() {
        Function<UserMessageDto, UserMessageDto> emailFunction = context.getBean("email", Function.class);
        UserMessageDto input = new UserMessageDto(1L, "John", "Doe", "john@example.com", "1234567890");
        UserMessageDto result = emailFunction.apply(input);

        assertEquals(input, result);
        assertEquals(input.id(), result.id());
        assertEquals(input.email(), result.email());
    }

    @Test
    void smsFunctionReturnsId() {
        Function<UserMessageDto, Long> smsFunction = context.getBean("sms", Function.class);
        UserMessageDto input = new UserMessageDto(42L, "John", "Doe", "john@example.com", "1234567890");
        Long result = smsFunction.apply(input);

        assertEquals(42L, result);
    }

    @Test
    void emailFunctionHandlesNullEmail() {
        Function<UserMessageDto, UserMessageDto> emailFunction = context.getBean("email", Function.class);
        UserMessageDto input = new UserMessageDto(1L, "John", "Doe", null, "1234567890");
        UserMessageDto result = emailFunction.apply(input);

        assertNull(result.email());
    }

    @Test
    void smsFunctionHandlesDifferentIds() {
        Function<UserMessageDto, Long> smsFunction = context.getBean("sms", Function.class);

        for (Long id : java.util.Arrays.asList(1L, 100L, 999999L)) {
            UserMessageDto input = new UserMessageDto(id, "Test", "User", "test@test.com", "5555555");
            Long result = smsFunction.apply(input);
            assertEquals(id, result);
        }
    }
}
