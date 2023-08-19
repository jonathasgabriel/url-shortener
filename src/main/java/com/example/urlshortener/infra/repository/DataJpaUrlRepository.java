package com.example.urlshortener.infra.repository;

import com.example.urlshortener.domain.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataJpaUrlRepository extends JpaRepository<Url, Long> {
    Url findByShortUrl(String shortUrl);
}
