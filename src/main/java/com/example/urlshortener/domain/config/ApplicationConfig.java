package com.example.urlshortener.domain.config;

import java.time.Duration;

public interface ApplicationConfig {
    Duration getRetentionPeriod();
    String getBaseUrl();
}
