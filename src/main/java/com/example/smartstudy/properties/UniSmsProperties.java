package com.example.smartstudy.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "zw.uni")
public class UniSmsProperties {
    private String accessKeyId;
    private String templateId;
    private String signature;
    private String ttl;
}
