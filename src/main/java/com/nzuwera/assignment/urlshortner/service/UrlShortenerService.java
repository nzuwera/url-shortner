package com.nzuwera.assignment.urlshortner.service;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.exception.AlreadyExistsException;
import com.nzuwera.assignment.urlshortner.exception.NotFoundException;
import com.nzuwera.assignment.urlshortner.model.DtoMapper;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlShortenerService implements IUrlShortenerService {
    private final ShortUrlRepository repository;

    @Override
    public ResponseObject<ShortUrl> create(CreateShortUrlRequest request) {
        log.info("Create Url link - {}", request);
        try {
            Optional<ShortUrl> urlOptional = repository.findByUrl(request.getUrl());
            if (urlOptional.isPresent())
                throw new AlreadyExistsException(String.format("ShortUrl already exists for URL = %s", request.getUrl()));
            ShortUrl shortUrl = DtoMapper.toEntity(request);
            // Save new short URL
            ShortUrl saved = repository.save(shortUrl);
            log.info("ShortUrl {} created successfully!", saved.getUrlId());
            return ResponseObject.<ShortUrl>builder()
                    .status(true)
                    .code(HttpStatus.OK.value())
                    .message(String.format("ShortUrl %s created successfully!", saved.getUrlId()))
                    .data(saved)
                    .build();
        } catch (Exception ex) {
            log.error("ShortUrl generation failed with reason - {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ShortUrl getById(String id) {
        return repository.findByUrlId(id).orElseThrow(() -> new NotFoundException(String.format("Url Id %s not found", id)));
    }
}
