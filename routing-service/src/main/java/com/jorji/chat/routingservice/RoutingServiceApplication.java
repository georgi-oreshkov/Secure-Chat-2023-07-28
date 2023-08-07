package com.jorji.chat.routingservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RoutingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoutingServiceApplication.class, args);
	}

}
