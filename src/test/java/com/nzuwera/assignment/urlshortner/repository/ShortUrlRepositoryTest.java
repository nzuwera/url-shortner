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
        shortUrl.setExpireTimestamp(LocalDateTime.now().plusHours(20));
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
        assertThat(foundUrl).isPresent(); // Check if value is present
        assertThat(foundUrl.get()).isNotNull();    // Verify the entity is not null
    }

    @Test
    @DisplayName("Find ShortUrl by Url")
    void findByUrl() {
        ShortUrl savedUrl = repository.save(shortUrl);
        assertThat(savedUrl).isNotNull();

        Optional<ShortUrl> foundUrl = repository.findByUrl(shortUrl.getUrl());
        assertThat(foundUrl).isPresent(); // Check if value is present
        assertThat(foundUrl.get()).isNotNull();    // Verify the entity is not null
    }
}