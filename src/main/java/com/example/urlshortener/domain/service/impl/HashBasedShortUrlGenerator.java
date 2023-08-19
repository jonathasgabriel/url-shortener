package com.example.urlshortener.domain.service.impl;

import com.example.urlshortener.domain.exception.UrlGenerationFailedException;
import com.example.urlshortener.domain.service.ShortUrlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Slf4j
@Component
public class HashBasedShortUrlGenerator implements ShortUrlGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 64;
    private static final int SALT_LENGTH = 6;
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    @Override
    public String generateShortUrl(String originalUrl) {
        byte[] saltBuffer = new byte[SALT_LENGTH];
        random.nextBytes(saltBuffer);

        String hashingSalt = encoder.encodeToString(saltBuffer);

        KeySpec spec = new PBEKeySpec(originalUrl.toCharArray(),
                hashingSalt.getBytes(),
                ITERATION_COUNT,
                KEY_LENGTH);

        SecretKeyFactory factory;

        try {
            factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();

            return encoder.encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage());

            throw new UrlGenerationFailedException();
        }
    }
}
