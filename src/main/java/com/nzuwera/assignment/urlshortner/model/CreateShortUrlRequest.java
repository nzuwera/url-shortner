package com.nzuwera.assignment.urlshortner.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateShortUrlRequest {
    @URL(message = "Invalid URL provided")
    @JsonProperty("url")
    private String url;
    @JsonProperty("shortUrlId")
    private String shortUrlId;
    @JsonProperty("ttl")
    private int ttl;
}
