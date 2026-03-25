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
    public Consumer<String> emailsSent(UserRepository userRepository) {
        return mobileNumber -> {
            userRepository.findByMobileNumber(mobileNumber).ifPresentOrElse(user -> {
                logger.info("Communications sent to user with mobile number: " + mobileNumber);
                user.setSentEmails(true);
                userRepository.save(user);
            }, () -> {throw new RuntimeException("Could not update user with mobile number " + mobileNumber);});
        };
    }

    @Bean
    public Consumer<Long> communicationsError() {
        return id -> {
            System.out.println("Communications error");
        };
    }

}
