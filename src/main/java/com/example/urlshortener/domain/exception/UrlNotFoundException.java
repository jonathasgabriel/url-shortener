package com.example.urlshortener.domain.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException() {
        super("This URL was not found");
    }
}
