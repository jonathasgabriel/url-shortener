package com.example.urlshortener.domain.repository;

import com.example.urlshortener.domain.model.Url;

public interface UrlRepository {

    Url create(Url url);
    Url findByShortUrl(String shortUrl);
    void delete(Url url);
    Url update(Url url);
}
