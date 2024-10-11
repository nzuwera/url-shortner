package com.nzuwera.assignment.urlshortner.service;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;

public interface IUrlShortenerService {
    ResponseObject<ShortUrl> create(CreateShortUrlRequest request);

    ShortUrl getById(String id);

    void deleteExpired();
}
