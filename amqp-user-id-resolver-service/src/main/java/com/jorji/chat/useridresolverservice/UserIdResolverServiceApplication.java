package com.jorji.chat.useridresolverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UserIdResolverServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserIdResolverServiceApplication.class, args);
	}

}
