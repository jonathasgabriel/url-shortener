package com.example.urlshortener.domain.exception;

public class UrlGenerationFailedException extends RuntimeException {

    public UrlGenerationFailedException() {
        super("It was not possible to shorten the URL due to an unexpected error");
    }
}
