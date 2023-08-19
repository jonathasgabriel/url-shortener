package com.example.urlshortener.domain.service;

import com.example.urlshortener.domain.dto.CreateUrlDto;
import com.example.urlshortener.domain.model.Url;

public interface UrlCreationService {
    Url create(CreateUrlDto createShortenedUrlDto);
}