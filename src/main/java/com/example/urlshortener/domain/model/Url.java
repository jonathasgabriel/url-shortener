package com.example.urlshortener.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "urls")
public class Url{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_url", unique = true, nullable = false)
    private String shortUrl;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "last_accessed_at", nullable = false)
    private LocalDateTime lastAccessedAt;

    public void wasAccessed() {
        this.lastAccessedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(id, url.id)
                && Objects.equals(shortUrl, url.shortUrl)
                && Objects.equals(originalUrl, url.originalUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortUrl, originalUrl);
    }
}
