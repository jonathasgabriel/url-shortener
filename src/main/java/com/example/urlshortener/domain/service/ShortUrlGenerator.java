package com.example.urlshortener.domain.service;

public interface ShortUrlGenerator {
    String generateShortUrl(String originalUrl);
}