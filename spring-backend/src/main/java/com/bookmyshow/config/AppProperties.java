package com.bookmyshow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@lombok.Getter
@lombok.Setter
public class AppProperties {

    private String jwtSecret = "change-me";
    private long jwtExpirationMs = 86400000L; // 1 day
    private String frontendUrl = "http://localhost:3000";
    private String stripeKey = "";
    private String resendApiKey = "";
}
