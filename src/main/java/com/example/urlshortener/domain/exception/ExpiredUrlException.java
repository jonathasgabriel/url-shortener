package com.example.urlshortener.domain.exception;

public class ExpiredUrlException extends RuntimeException {

    public ExpiredUrlException() {
        super("This URL was expired and is no longer accessible.");
    }
}
