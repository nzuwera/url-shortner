package com.nzuwera.assignment.urlshortner.model;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.utils.Utils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class DtoMapper {
    private DtoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ShortUrl toEntity(CreateShortUrlRequest request) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setUrl(request.getUrl());
        shortUrl.setUrlId(StringUtils.hasText(request.getShortUrlId()) ? request.getShortUrlId() : Utils.generateUrlId());
        shortUrl.setExpireTimestamp(request.getTtl() == 0 ? LocalDateTime.MAX : LocalDateTime.now().plusHours(request.getTtl()));
        return shortUrl;
    }
}
