package com.example.urlshortener.infra.config;

import com.example.urlshortener.domain.config.ApplicationConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("urlshortener")
public class ApplicationConfigImpl implements ApplicationConfig {
    private Duration retentionPeriod;
    private String baseUrl;
}
