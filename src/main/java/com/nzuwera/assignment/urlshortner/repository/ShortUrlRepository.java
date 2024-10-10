package com.nzuwera.assignment.urlshortner.repository;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByUrlId(String urlId);
    Optional<ShortUrl> findByUrl(String Url);
}
