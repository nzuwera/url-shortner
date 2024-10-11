package com.nzuwera.assignment.urlshortner.service;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.exception.AlreadyExistsException;
import com.nzuwera.assignment.urlshortner.exception.NotFoundException;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.DtoMapper;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private UrlShortenerService shortUrlService;
    private ShortUrl shortUrl;
    private CreateShortUrlRequest request;

    @BeforeEach
    void setup() {
        shortUrl = new ShortUrl();
        request = CreateShortUrlRequest.builder().build();
    }

    @Test
    @DisplayName("Create shortUrl successful using originalUrl only!")
    void UrlShortenerService_CreateShortUrl_ReturnResponseObjectShortUrl_001() {
        String originalUrl = "https://google.com";
        request.setUrl(originalUrl);
        shortUrl = DtoMapper.toEntity(request);
        when(shortUrlRepository.save(Mockito.any(ShortUrl.class))).thenReturn(shortUrl);

        ResponseObject<ShortUrl> createdUrl = shortUrlService.create(request);
        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.isStatus()).isTrue();
        assertThat(createdUrl.getCode()).isEqualTo(200);
        assertThat(createdUrl.getData()).isNotNull();
        assertThat(createdUrl.getData().getUrl()).isEqualTo(originalUrl);
        assertThat(createdUrl.getData().getExpireTimestamp()).isEqualTo(shortUrl.getExpireTimestamp());
    }

    @Test
    @DisplayName("Create shortUrl successful using originalUrl and UrlId!")
    void UrlShortenerService_CreateShortUrl_ReturnResponseObjectShortUrl_002() {
        String originalUrl = "https://google.com";
        String desiredUrlId = "GGL12345";
        request.setUrl(originalUrl);
        request.setShortUrlId(desiredUrlId);
        shortUrl = DtoMapper.toEntity(request);
        when(shortUrlRepository.save(Mockito.any(ShortUrl.class))).thenReturn(shortUrl);

        ResponseObject<ShortUrl> createdUrl = shortUrlService.create(request);

        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.isStatus()).isTrue();
        assertThat(createdUrl.getCode()).isEqualTo(200);
        assertThat(createdUrl.getData()).isNotNull();
        assertThat(createdUrl.getData().getUrl()).isEqualTo(originalUrl);
        assertThat(createdUrl.getData().getExpireTimestamp()).isEqualTo(shortUrl.getExpireTimestamp());
        assertThat(createdUrl.getData().getUrlId()).isEqualTo(desiredUrlId);
        assertThat(createdUrl.getMessage()).isEqualTo(String.format("ShortUrl %s created successfully!", desiredUrlId));
    }
    @Test
    @DisplayName("Create shortUrl successful using originalUrl, UrlId and ttl!")
    void UrlShortenerService_CreateShortUrl_ReturnResponseObjectShortUrl_003() {
        String originalUrl = "https://google.com";
        String desiredUrlId = "GGL12345";
        int ttl = 10;
        request.setUrl(originalUrl);
        request.setShortUrlId(desiredUrlId);
        request.setTtl(ttl);
        shortUrl = DtoMapper.toEntity(request);
        when(shortUrlRepository.save(Mockito.any(ShortUrl.class))).thenReturn(shortUrl);

        ResponseObject<ShortUrl> createdUrl = shortUrlService.create(request);

        assertThat(createdUrl).isNotNull();
        assertThat(createdUrl.isStatus()).isTrue();
        assertThat(createdUrl.getCode()).isEqualTo(200);
        assertThat(createdUrl.getData()).isNotNull();
        assertThat(createdUrl.getData().getUrl()).isEqualTo(originalUrl);
        assertThat(createdUrl.getData().getExpireTimestamp()).isEqualTo(shortUrl.getExpireTimestamp());
    }


    @Test
    @DisplayName("Create shortUrl return AlreadyExistException")
    void UrlShortenerService_CreateShortUrl_ReturnAlreadyExistException() {
        String originalUrl = "https://google.com";
        String desiredUrlId = "GGL12345";
        int ttl = 10;
        request.setUrl(originalUrl);
        request.setShortUrlId(desiredUrlId);
        request.setTtl(ttl);
        shortUrl = DtoMapper.toEntity(request);
        when(shortUrlRepository.findByUrl(originalUrl)).thenReturn(Optional.of(shortUrl));

        Exception exception = assertThrows(AlreadyExistsException.class, () -> shortUrlService.create(request));

        String expectedMessage = String.format("ShortUrl already exists for URL = %s", request.getUrl());
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void UrlShortenerService_GetById_ReturnShortUrl() {
        // Arrange
        String originalUrl = "https://google.com";
        String desiredUrlId = "GGL12345";
        int ttl = 10;
        request.setUrl(originalUrl);
        request.setShortUrlId(desiredUrlId);
        request.setTtl(ttl);
        shortUrl = DtoMapper.toEntity(request);

        when(shortUrlRepository.findByUrlId(desiredUrlId)).thenReturn(Optional.of(shortUrl));

        // Act
        ShortUrl result = shortUrlService.getById(desiredUrlId);

        // Assert
        assertNotNull(result);
        assertEquals(shortUrl, result);
        verify(shortUrlRepository, times(1)).findByUrlId(desiredUrlId); // Ensure repository was called once
    }

    @Test
    void UrlShortenerService_GetById_ReturnNotFoundException() {
        String originalUrl = "https://google.com";
        String desiredUrlId = "GGL12345";
        String unknownUrlId = "GGL123456";
        int ttl = 10;
        request.setUrl(originalUrl);
        request.setShortUrlId(desiredUrlId);
        request.setTtl(ttl);
        shortUrl = DtoMapper.toEntity(request);

        when(shortUrlRepository.findByUrlId(unknownUrlId)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> shortUrlService.getById(unknownUrlId));
        assertEquals(String.format("Url Id %s not found",unknownUrlId), exception.getMessage());

        verify(shortUrlRepository, times(1)).findByUrlId(unknownUrlId); // Ensure repository was called once
    }

    @Test
    void UrlShortenerService_DeleteExpiredUrls(){
            // Act
            shortUrlService.deleteExpired();

            // Assert
            verify(shortUrlRepository, times(1)).deleteByExpireTimestampBefore(any(LocalDateTime.class));

    }
}