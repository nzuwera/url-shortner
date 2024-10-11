package com.nzuwera.assignment.urlshortner.repository;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByUrlId(String urlId);
    Optional<ShortUrl> findByUrl(String url);
    @Modifying
    void deleteByExpireTimestampBefore(LocalDateTime now);

    @Modifying
    void deleteByUrlId(String id);
}
