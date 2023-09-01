package com.jorji.chat.routingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.jorji.chat.user-resolution")
@Data
public class UserResolverServiceProperties {
    private String host;
    private int port;
    private String apiPath;
}
