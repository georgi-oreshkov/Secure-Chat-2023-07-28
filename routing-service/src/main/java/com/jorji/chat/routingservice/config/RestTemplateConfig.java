package com.jorji.chat.routingservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new MessagePackHttpMessageConverter());

        return restTemplate;
    }

    private static class MessagePackHttpMessageConverter extends AbstractJackson2HttpMessageConverter {
        MessagePackHttpMessageConverter() {
            super(new ObjectMapper(new MessagePackFactory()));
            setSupportedMediaTypes(List.of(MediaType.valueOf("application/x-msgpack")));
        }
    }
}