package com.umcs.enterprise.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.springdoc")
public class OpenApiConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return  new ObjectMapper();
    }
}
