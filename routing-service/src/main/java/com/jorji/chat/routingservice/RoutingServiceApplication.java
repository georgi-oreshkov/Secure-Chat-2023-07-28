package com.jorji.chat.routingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RoutingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoutingServiceApplication.class, args);
	}

}
