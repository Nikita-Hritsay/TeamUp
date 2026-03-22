package org.message.service.message.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;


@Configuration
public class MessageConfiguration {

    @KafkaListener(id="email", topicPartitions = {@TopicPartition(topic="send-userCreationNotifications", partitions = "0")})
    public void email(String email) {
        System.out.println("Email is sent to "  + email);
    }

    @KafkaListener(id="sms", topicPartitions = {@TopicPartition(topic = "send-userCreationNotifications", partitions = {"1"})})
    public void sms(String mobileNumber) {
        System.out.println("Sms is sent to "  + mobileNumber);
    }

}
