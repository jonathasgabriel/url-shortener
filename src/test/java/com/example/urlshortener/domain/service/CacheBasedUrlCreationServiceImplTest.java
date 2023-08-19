package com.example.urlshortener.domain.service;

import com.example.urlshortener.domain.dto.CreateUrlDto;
import com.example.urlshortener.domain.exception.UrlGenerationFailedException;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.repository.UrlRepository;
import com.example.urlshortener.domain.service.impl.CacheBasedUrlCreationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class CacheBasedUrlCreationServiceImplTest {

    @MockBean
    private UrlRepository shortenedUrlRepository;

    @MockBean
    private ShortUrlGenerator shortUrlGenerator;

    @Autowired
    private CacheBasedUrlCreationServiceImpl urlCreationService;

    @Test
    public void itCreatesShortenedUrl() {
        // given
        String originalUrl = "http://original-url.com";
        CreateUrlDto createShortenedUrlDto = new CreateUrlDto(originalUrl);
        String shortUrl = "short-url";
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        when(shortUrlGenerator.generateShortUrl(originalUrl)).thenReturn(shortUrl);
        when(shortenedUrlRepository.create(url)).thenReturn(url);

        // when
        Url createdUrl = urlCreationService.create(createShortenedUrlDto);

        // then
        assertEquals(shortUrl, createdUrl.getShortUrl());
        assertEquals(originalUrl, createdUrl.getOriginalUrl());
        assertEquals(lastAccessedAt, createdUrl.getLastAccessedAt());
    }

    @Test
    public void itThrowsWhenShortenedUrlGenerationFails() {
        // given
        String originalUrl = "http://original-url.com";
        CreateUrlDto createShortenedUrlDto = new CreateUrlDto(originalUrl);

        doThrow(UrlGenerationFailedException.class).when(shortUrlGenerator).generateShortUrl(originalUrl);

        // then
        assertThrows(UrlGenerationFailedException.class,
                () -> urlCreationService.create(createShortenedUrlDto));
    }

    @Test
    public void itThrowsWhenShortenedUrlFailsToBePersisted() {
        // given
        String originalUrl = "http://original-url.com";
        CreateUrlDto createShortenedUrlDto = new CreateUrlDto(originalUrl);
        String shortUrl = "short-url";

        when(shortUrlGenerator.generateShortUrl(anyString())).thenReturn(shortUrl);

        doThrow(RuntimeException.class).when(shortenedUrlRepository).create(any(Url.class));

        // then
        assertThrows(Exception.class,
                () -> urlCreationService.create(createShortenedUrlDto));
    }
}
