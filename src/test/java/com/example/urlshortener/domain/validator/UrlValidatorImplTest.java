package com.example.urlshortener.domain.validator;

import com.example.urlshortener.domain.config.ApplicationConfig;
import com.example.urlshortener.domain.model.Url;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class UrlValidatorImplTest {

    @MockBean
    private ApplicationConfig applicationConfig;

    @Autowired
    private UrlValidator validator;

    @Test
    public void itReturnsFalseIfUrlIsNotExpired() {
        // given
        Duration retentionPeriod = Duration.ofHours(6L);
        LocalDateTime lastAccessedAt = LocalDateTime.now().minusHours(5L);
        Url url = Url.builder().lastAccessedAt(lastAccessedAt).build();

        when(applicationConfig.getRetentionPeriod()).thenReturn(retentionPeriod);

        // when
        boolean isExpired = validator.isUrlExpired(url);

        assertFalse(isExpired);
    }

    @Test
    public void itReturnsTrueIfUrlIsExpired() {
        // given
        Duration retentionPeriod = Duration.ofHours(6L);
        LocalDateTime lastAccessedAt = LocalDateTime.now().minusHours(7L);
        Url url = Url.builder().lastAccessedAt(lastAccessedAt).build();

        when(applicationConfig.getRetentionPeriod()).thenReturn(retentionPeriod);

        // when
        boolean isExpired = validator.isUrlExpired(url);

        assertTrue(isExpired);
    }

    @Test
    public void itThrowsWhenRetentionPeriodConfigIsNotSet() {
        // given
        Url url = Url.builder().lastAccessedAt(LocalDateTime.now()).build();

        when(applicationConfig.getRetentionPeriod()).thenReturn(null);

        // then
        assertThrows(RuntimeException.class,
                () -> validator.isUrlExpired(url));
    }
}
