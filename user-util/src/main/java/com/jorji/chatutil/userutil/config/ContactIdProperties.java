package com.jorji.chatutil.userutil.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("chat.contact.contact-id")
@Component
@Getter
@Setter
public class ContactIdProperties {
    private int length;
    private int beginningCharsLength;
}
