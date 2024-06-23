package com.example.smartstudy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "zw.jwt")
public class JwtProperties {

    //生成jwt令牌的配置
    private String orSecretKey;
    private long orTtl;
    private String orTokenName;

}
