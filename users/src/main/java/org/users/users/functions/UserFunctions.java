package org.users.users.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.users.users.repository.UserRepository;

import java.util.function.Consumer;

@Configuration
public class UserFunctions {

    private static Logger logger = LoggerFactory.getLogger(UserFunctions.class);

    @Bean
    public Consumer<Long> emailsSent(UserRepository userRepository) {
        return id -> {
            userRepository.findById(id).ifPresentOrElse(user -> {
                logger.info("Communications sent to user " + id);
                user.setSentEmails(true);
                userRepository.save(user);
            }, () -> {throw new RuntimeException("Could not update user with id " + id);});
        };
    }


}
