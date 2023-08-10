package com.jorji.chat.routingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.jorji.chat.amqp")
@Data
public class AmqpConfig {
    private String userResolutionQueueName;
    private String groupResolutionQueueName;
    private String directMessageQueueName;
    private String contactResolutionQueueName;
}
