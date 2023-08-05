package com.jorji.chat.usermanagementservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("chat.contact.contact-id")
@Getter
@Setter
public class ContactIdProperties {
    private int length;
    private int beginningCharsLength;
}
