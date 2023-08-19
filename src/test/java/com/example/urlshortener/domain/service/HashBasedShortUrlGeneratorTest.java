package com.example.urlshortener.domain.service;

import com.example.urlshortener.domain.service.impl.HashBasedShortUrlGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class HashBasedShortUrlGeneratorTest {

    @Autowired
    private HashBasedShortUrlGenerator urlGenerator;

    @Test
    public void itGeneratesHashBasedShortUrl() {
        // given
        String originalUrl = "http://original-url-that-is-really-very-long";
        int expectedHashUrlSize = 11;

        // when
        String hashUrl = urlGenerator.generateShortUrl(originalUrl);

        // then
        assertEquals(expectedHashUrlSize, hashUrl.length());
    }
}
