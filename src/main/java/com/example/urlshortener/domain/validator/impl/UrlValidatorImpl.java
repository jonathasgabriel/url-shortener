package com.example.urlshortener.domain.validator.impl;

import com.example.urlshortener.domain.config.ApplicationConfig;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.validator.UrlValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class UrlValidatorImpl implements UrlValidator {

    private ApplicationConfig applicationConfig;

    @Override
    public boolean isUrlExpired(Url url) {
        LocalDateTime urlLastAccessedAt = url.getLastAccessedAt();
        LocalDateTime now = LocalDateTime.now();
        Duration durationSinceLastAccess = Duration.between(urlLastAccessedAt, now);
        Duration retentionPeriod = applicationConfig.getRetentionPeriod();

        boolean isUrlExpired = durationSinceLastAccess.compareTo(retentionPeriod) > 0;

        return isUrlExpired;
    }
}
