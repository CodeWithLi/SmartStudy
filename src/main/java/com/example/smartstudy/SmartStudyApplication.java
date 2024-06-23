package com.example.smartstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.example.smartstudy.mapper")
@EnableConfigurationProperties
@EnableCaching
public class SmartStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartStudyApplication.class, args);
    }

}
