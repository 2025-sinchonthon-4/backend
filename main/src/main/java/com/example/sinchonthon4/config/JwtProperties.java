package com.example.sinchonthon4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="jwt")
public class JwtProperties {
    private String header;
    private String secret;
    private long accessTokenValidityInSeconds;
    private long refreshTokenValidityInSeconds;
}
