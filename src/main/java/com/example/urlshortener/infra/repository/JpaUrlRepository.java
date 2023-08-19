package com.example.urlshortener.infra.repository;

import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.repository.UrlRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class JpaUrlRepository implements UrlRepository {

    private DataJpaUrlRepository repository;

    @Override
    public Url create(Url url) {
        return repository.save(url);
    }

    @Override
    public Url findByShortUrl(String url) {
        return repository.findByShortUrl(url);
    }

    @Override
    public void delete(Url url) {
        repository.delete(url);
    }

    @Override
    public Url update(Url url) {
        return repository.save(url);
    }
}
