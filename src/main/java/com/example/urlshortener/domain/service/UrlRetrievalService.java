package com.example.urlshortener.domain.service;

public interface UrlRetrievalService {
    String retrieveOriginalUrl(String shortenedUrl);
}