package com.example.urlshortener.domain.validator;

import com.example.urlshortener.domain.model.Url;

public interface UrlValidator {
    boolean isUrlExpired(Url url);
}
