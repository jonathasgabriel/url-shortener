package com.example.urlshortener.domain.service.impl;

import com.example.urlshortener.domain.exception.ExpiredUrlException;
import com.example.urlshortener.domain.exception.UrlNotFoundException;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.repository.UrlRepository;
import com.example.urlshortener.domain.service.UrlRetrievalService;
import com.example.urlshortener.domain.validator.UrlValidator;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CacheBasedUrlRetrievalService implements UrlRetrievalService {

    public static final String CACHE_NAME = "urls";

    private UrlRepository shortenedUrlRepository;
    private UrlValidator validator;

    @Override
    @Cacheable(CACHE_NAME)
    public String retrieveOriginalUrl(String shortenedUrl) {
        Url url = shortenedUrlRepository.findByShortUrl(shortenedUrl);

        if (url == null) {
            throw new UrlNotFoundException();
        }

        boolean isUrlExpired = validator.isUrlExpired(url);
        if (isUrlExpired) {
            shortenedUrlRepository.delete(url);

            throw new ExpiredUrlException();
        }

        url.wasAccessed();

        url = shortenedUrlRepository.update(url);

        return url.getOriginalUrl();
    }
}
