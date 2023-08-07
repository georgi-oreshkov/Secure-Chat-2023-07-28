package com.jorji.chat.routingservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.jorji.chat.uuid-resolver-service")
@Data
public class UserResolverServiceProperties {
    private String host;
    private int port;
    private String apiPath;

    public String getUrlForUser(String username) {
        return String.format(
                "http://%s:%d%s/username/%s",
                host,
                port,
                apiPath,
                username
        );
    }

    public String getUrlForContact(String contactId) {
        return String.format(
                "http://%s:%d%s/contact/%s",
                host,
                port,
                apiPath,
                contactId
        );
    }
}
