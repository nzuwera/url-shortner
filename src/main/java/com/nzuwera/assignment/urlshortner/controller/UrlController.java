package com.nzuwera.assignment.urlshortner.controller;

import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.service.IUrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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
    @Operation(summary = "Create a short URL", description = "Creates a shortened version of the given URL with an optional custom ID and TTL.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Short URL created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid input"),
            @ApiResponse(responseCode = "409", description = "Custom ID already in use")
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<ShortUrl>> createShortUrl(@Valid @RequestBody CreateShortUrlRequest request) {
        ResponseObject<ShortUrl> response = shortenerService.create(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @Operation(summary = "Redirect to the original URL", description = "Fetches the original URL based on the short URL ID and redirects the client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to the original URL"),
            @ApiResponse(responseCode = "404", description = "Short URL not found or expired")
    })
    @GetMapping(value = "{id}")
    public RedirectView getShortUrl(@PathVariable String id) {
        ShortUrl shortUrl = shortenerService.getById(id);
        return new RedirectView(shortUrl.getUrl());
    }


    @Operation(summary = "Delete a short URL", description = "Deletes the short URL with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Short URL deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Short URL not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String id) {
        shortenerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
