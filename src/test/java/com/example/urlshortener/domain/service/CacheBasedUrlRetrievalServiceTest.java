package com.example.urlshortener.domain.service;

import com.example.urlshortener.domain.exception.ExpiredUrlException;
import com.example.urlshortener.domain.exception.UrlNotFoundException;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.repository.UrlRepository;
import com.example.urlshortener.domain.service.impl.CacheBasedUrlRetrievalService;
import com.example.urlshortener.domain.validator.UrlValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class CacheBasedUrlRetrievalServiceTest {

    @MockBean
    private UrlRepository repository;

    @MockBean
    private UrlValidator validator;

    @Autowired
    private CacheBasedUrlRetrievalService service;

    @Test
    public void itRetrievesOriginalUrl() {
        // given
        String originalUrl = "http://original-url.com";
        String shortUrl = "short-url";
        boolean urlExpired = false;
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        when(repository.findByShortUrl(shortUrl)).thenReturn(url);
        when(validator.isUrlExpired(url)).thenReturn(urlExpired);
        doNothing().when(repository).delete(url);
        when(repository.update(url)).thenReturn(url);

        // when
        String retrievedUrl = service.retrieveOriginalUrl(shortUrl);

        // then
        assertEquals(originalUrl, retrievedUrl);
    }

    @Test
    public void itThrowsWhenFindShortenedUrlFails() {
        // given
        String shortUrl = "short-url";

        doThrow(RuntimeException.class).when(repository).findByShortUrl(shortUrl);

        // then
        assertThrows(Exception.class,
                () -> service.retrieveOriginalUrl(shortUrl));
    }

    @Test
    public void itThrowsUrlNotFoundExceptionWhenShortenedUrlDoesNotExist() {
        // given
        String shortUrl = "short-url";

        when(repository.findByShortUrl(shortUrl)).thenReturn(null);

        // then
        assertThrows(UrlNotFoundException.class,
                () -> service.retrieveOriginalUrl(shortUrl));
    }

    @Test
    public void itThrowsWhenUrlIsExpired() {
        // given
        String shortUrl = "short-url";
        Url url = Url.builder().build();

        when(repository.findByShortUrl(shortUrl)).thenReturn(url);
        doThrow(RuntimeException.class).when(validator).isUrlExpired(url);

        // then
        assertThrows(RuntimeException.class,
                () -> service.retrieveOriginalUrl(shortUrl));
    }

    @Test
    public void itThrowsWhenExpiredShortenedUrlDeletionFails() {
        // given
        String shortUrl = "short-url";
        Url url = Url.builder().build();
        boolean urlExpired = true;

        when(repository.findByShortUrl(shortUrl)).thenReturn(url);
        when(validator.isUrlExpired(url)).thenReturn(urlExpired);

        doThrow(RuntimeException.class).when(repository).delete(url);

        // then
        assertThrows(RuntimeException.class,
                () -> service.retrieveOriginalUrl(shortUrl));
    }

    @Test
    public void itDeletesShortenedUrlAndThrowsExceptionWhenExpiredUrlGetsAccessed() {
        // given
        String shortUrl = "short-url";
        Url url = Url.builder().build();
        boolean urlExpired = true;

        when(repository.findByShortUrl(shortUrl)).thenReturn(url);
        when(validator.isUrlExpired(url)).thenReturn(urlExpired);

        doNothing().when(repository).delete(url);

        // then
        assertThrows(ExpiredUrlException.class,
                () -> service.retrieveOriginalUrl(shortUrl));

        verify(repository, times(1)).delete(url);
    }

    @Test
    public void itThrowsWhenAccessedShortenedUrlUpdateFails() {
        // given
        String shortUrl = "short-url";
        Url url = Url.builder().build();
        boolean urlExpired = false;

        when(repository.findByShortUrl(shortUrl)).thenReturn(url);
        when(validator.isUrlExpired(url)).thenReturn(urlExpired);
        doNothing().when(repository).delete(url);

        doThrow(RuntimeException.class).when(repository).update(url);

        // then
        assertThrows(RuntimeException.class,
                () -> service.retrieveOriginalUrl(shortUrl));
    }

}
