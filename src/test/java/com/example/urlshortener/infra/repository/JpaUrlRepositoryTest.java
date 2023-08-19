package com.example.urlshortener.infra.repository;

import com.example.urlshortener.domain.model.Url;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class JpaUrlRepositoryTest {


    @MockBean
    private DataJpaUrlRepository dataJpaUrlRepository;

    @Autowired
    private JpaUrlRepository repository;

    @Test
    public void itCreatesUrl() {
        // given
        String originalUrl = "http://original-url.com";
        String shortUrl = "short-url";
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        when(dataJpaUrlRepository.save(url)).thenReturn(url);

        // when
        Url createdUrl = repository.create(url);

        // then
        assertEquals(shortUrl, createdUrl.getShortUrl());
        assertEquals(originalUrl, createdUrl.getOriginalUrl());
        assertEquals(lastAccessedAt, createdUrl.getLastAccessedAt());
    }

    @Test
    public void itFindsUrlByShortUrl() {
        // given
        String originalUrl = "http://original-url.com";
        String shortUrl = "short-url";
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        when(dataJpaUrlRepository.findByShortUrl(shortUrl)).thenReturn(url);

        // when
        Url foundUrl = repository.findByShortUrl(shortUrl);

        // then
        assertEquals(shortUrl, foundUrl.getShortUrl());
        assertEquals(originalUrl, foundUrl.getOriginalUrl());
        assertEquals(lastAccessedAt, foundUrl.getLastAccessedAt());
    }

    @Test
    public void itDeletesUrl() {
        // given
        String originalUrl = "http://original-url.com";
        String shortUrl = "short-url";
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        doNothing().when(dataJpaUrlRepository).delete(url);

        // when
        repository.delete(url);

        // then
        verify(dataJpaUrlRepository, times(1)).delete(url);
    }

    @Test
    public void itUpdatesUrl() {
        // given
        String originalUrl = "http://original-url.com";
        String shortUrl = "short-url";
        LocalDateTime lastAccessedAt = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(lastAccessedAt)
                .build();

        when(dataJpaUrlRepository.save(url)).thenReturn(url);

        // when
        Url updatedUrl = repository.update(url);

        // then
        assertEquals(shortUrl, updatedUrl.getShortUrl());
        assertEquals(originalUrl, updatedUrl.getOriginalUrl());
        assertEquals(lastAccessedAt, updatedUrl.getLastAccessedAt());
    }
}
