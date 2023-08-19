package com.example.urlshortener.domain.service.impl;

import com.example.urlshortener.domain.dto.CreateUrlDto;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.repository.UrlRepository;
import com.example.urlshortener.domain.service.ShortUrlGenerator;
import com.example.urlshortener.domain.service.UrlCreationService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.urlshortener.domain.service.impl.CacheBasedUrlRetrievalService.CACHE_NAME;

@AllArgsConstructor
@Service
public class CacheBasedUrlCreationServiceImpl implements UrlCreationService {

    private UrlRepository shortenedUrlRepository;
    private ShortUrlGenerator shortUrlGenerator;
    private CacheManager cacheManager;

    @Override
    public Url create(CreateUrlDto createShortenedUrlDto) {
        String originalUrl = createShortenedUrlDto.getUrl();
        String shortUrl = shortUrlGenerator.generateShortUrl(originalUrl);

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .lastAccessedAt(LocalDateTime.now())
                .build();


        url = shortenedUrlRepository.create(url);

        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(shortUrl, originalUrl);
        }

        return url;
    }
}
