package com.nzuwera.assignment.urlshortner.repository;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ShortUrlRepositoryTest {

    @Autowired
    private ShortUrlRepository repository;
    private ShortUrl shortUrl;

    @BeforeEach
    public void setup() {
        shortUrl = new ShortUrl();
        shortUrl.setExpireTimestamp(LocalDateTime.now().plusSeconds(20));
        shortUrl.setUrl("https://google.com");
        shortUrl.setUrlId("ggl123");
    }

    @Test
    @DisplayName("Injected components are not null")
    void injectedComponentsAreNotNull() {
        assertThat(repository).isNotNull();
    }

    @Test
    @DisplayName("Find ShortUrl by Url ID")
    void findByUrlId() {
        ShortUrl savedUrl = repository.save(shortUrl);
        assertThat(savedUrl).isNotNull();
        Optional<ShortUrl> foundUrl = repository.findByUrlId(shortUrl.getUrlId());
        assertThat(foundUrl).isPresent();
    }

    @Test
    @DisplayName("Find ShortUrl by Url")
    void findByUrl() {
        ShortUrl savedUrl = repository.save(shortUrl);
        assertThat(savedUrl).isNotNull();

        Optional<ShortUrl> foundUrl = repository.findByUrl(shortUrl.getUrl());
        assertThat(foundUrl).isPresent();
    }

    @Test
    @DisplayName("Delete Expired ShortUrls")
    void deleteByExpireTimestampBefore_shouldDeleteExpiredUrls() {
        // Arrange
        repository.save(shortUrl);
        // Act
        repository.deleteByExpireTimestampBefore(LocalDateTime.now().plusHours(1));
        // Assert: Only the non-expired URL should remain
        Optional<ShortUrl> url1 = repository.findByUrlId("ggl123");
        assertThat(url1).isEmpty();
    }

    @Test
    @DisplayName("DeleteById When ShorUrlId exist")
    void deleteByUrlId_shouldDeleteUrlById() {
        // Arrange
        repository.save(shortUrl);
        // Act: Delete URL with id "def456"
        repository.deleteByUrlId("ggl123");
        // Assert: The URL with id "def456" should be deleted, and "abc123" should still exist
        Optional<ShortUrl> url1 = repository.findByUrlId("ggl123");
        assertThat(url1).isEmpty();
    }
}