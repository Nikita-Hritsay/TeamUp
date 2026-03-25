package org.message.service.message.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;


@Configuration
public class MessageConfiguration {

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public MessageConfiguration(KafkaTemplate<Integer, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id="email", topics="send-userCreationNotificationEmail")
    public void email(String email) {
        System.out.println("Email is sent to "  + email);
        kafkaTemplate.send("communication-sent", email);
    }

    @KafkaListener(id="sms", topics="send-userCreationNotificationSms")
    public void sms(String mobileNumber) {
        System.out.println("Sms is sent to "  + mobileNumber);
        kafkaTemplate.send("communication-sent", mobileNumber);
    }

}
