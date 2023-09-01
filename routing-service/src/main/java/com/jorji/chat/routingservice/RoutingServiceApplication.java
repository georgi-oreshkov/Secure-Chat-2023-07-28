package com.jorji.chat.routingservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketSession;

import java.util.AbstractMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan("com.jorji")
@EnableRabbit
@EnableRedisRepositories
public class RoutingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoutingServiceApplication.class, args);
	}

	@Bean(name = "sessionMap")
	public AbstractMap<UUID, WebSocketSession> getSessionMap(){
		return new ConcurrentHashMap<>();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
