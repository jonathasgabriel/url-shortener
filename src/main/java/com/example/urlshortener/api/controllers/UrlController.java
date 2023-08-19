package com.example.urlshortener.api.controllers;

import com.example.urlshortener.domain.config.ApplicationConfig;
import com.example.urlshortener.domain.dto.CreateUrlDto;
import com.example.urlshortener.domain.dto.UrlDto;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.service.UrlCreationService;
import com.example.urlshortener.domain.service.UrlRetrievalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/")
public class UrlController {

    private UrlCreationService creationService;
    private UrlRetrievalService retrievalService;
    private ApplicationConfig applicationConfig;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UrlDto createShortenedUrl(@RequestBody CreateUrlDto createShortenedUrlDto) {
        Url url = creationService.create(createShortenedUrlDto);

        String shortUrl = url.getShortUrl();
        String baseUrl = applicationConfig.getBaseUrl();

        String appUrlLink = new StringBuilder(baseUrl).append(shortUrl).toString();
        UrlDto urlDto = new UrlDto(appUrlLink);

        return urlDto;
    }

    @GetMapping("/{shortenedUrl}")
    public ResponseEntity accessShortenedUrl(@PathVariable String shortenedUrl) {
        String originalUrl = retrievalService.retrieveOriginalUrl(shortenedUrl);
        URI uri = URI.create(originalUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }
}
