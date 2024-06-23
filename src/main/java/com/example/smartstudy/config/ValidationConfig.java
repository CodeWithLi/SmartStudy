package com.example.smartstudy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
@ComponentScan("com.example.smartstudy.model.dto")
public class ValidationConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }
}
