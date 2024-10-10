package com.nzuwera.assignment.urlshortner.controller;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.service.IUrlShortenerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@RestController
@Validated
public class UrlController {
    private final IUrlShortenerService shortenerService;

    /**
     * Method allow generation of short url ID
     *
     * @param request GenerateUrlRequest
     * @return ResponseObject<ShortUrl>
     */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<ShortUrl>> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        ResponseObject<ShortUrl> response = shortenerService.create(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
