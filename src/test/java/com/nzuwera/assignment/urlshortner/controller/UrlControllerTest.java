package com.nzuwera.assignment.urlshortner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.exception.NotFoundException;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.DtoMapper;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UrlController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UrlControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService service;

    @Autowired
    private ObjectMapper objectMapper;
    private CreateShortUrlRequest request;
    private ShortUrl shortUrl;
    private ResponseObject<ShortUrl> responseObject;

    @BeforeEach
    void setUp() {
        shortUrl = new ShortUrl();
        request = CreateShortUrlRequest.builder().build();
        request.setUrl("https://google.com");
        responseObject = ResponseObject.<ShortUrl>builder().build();
    }

    @Test
    @DisplayName("Create ShorUrl from original URL only")
    void UrlController_CreateShortUrl_ReturnCreatedResponseObject_001() throws Exception {
        // Arrange
        shortUrl = DtoMapper.toEntity(request);
        responseObject.setStatus(true);
        responseObject.setCode(200);
        responseObject.setMessage(String.format("ShortUrl %s created successfully!", shortUrl.getUrlId()));
        responseObject.setData(shortUrl);
        when(service.create(request)).thenReturn(responseObject);

        // Act & Assert
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    @DisplayName("Create ShorUrl from original and ShorUrlId")
    void UrlController_CreateShortUrl_ReturnCreatedResponseObject_002() throws Exception {
        // Arrange
        request.setShortUrlId("GGL1234567");
        shortUrl = DtoMapper.toEntity(request);
        responseObject.setStatus(true);
        responseObject.setCode(200);
        responseObject.setMessage("ShortUrl GGL1234567 created successfully!");
        responseObject.setData(shortUrl);
        when(service.create(request)).thenReturn(responseObject);

        // Act & Assert
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").value("ShortUrl GGL1234567 created successfully!"));
    }

    @Test
    @DisplayName("Get ShortUrl Should redirect to original URL")
    void UrlController_GetShortUrl_ShouldRedirect_To_OriginalUrl() throws Exception {
        // Arrange
        String shorUrlId = "GGL1234567";
        request.setShortUrlId(shorUrlId);
        shortUrl = DtoMapper.toEntity(request);
        when(service.getById(shorUrlId)).thenReturn(shortUrl);

        // Act & Assert
        mockMvc.perform(get("/"+shorUrlId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(shortUrl.getUrl()));

        verify(service, times(1)).getById(shorUrlId);
    }

    @Test
    @DisplayName("Get ShortUrl Should return NotFoundException when ShortUrlId is not found")
    void UrlController_GetShortUrl_ShouldReturnNotFoundException_WhenShortUrlId_Is_NotFound() throws Exception {
        // Arrange
        String id = "nonExistingId";

        when(service.getById(id)).thenThrow(new NotFoundException("Url Id " + id + " not found"));

        // Act & Assert
        mockMvc.perform(get("/" + id))
                .andExpect(status().isNotFound());
        verify(service, times(1)).getById(id);
    }

    @Test
    @DisplayName("Delete ShortUrl Should return HttpStatus 204")
    void deleteShortUrl_shouldReturn204_whenUrlIsDeleted() throws Exception {
        // Arrange
        String id = "abc123";

        // Act & Assert
        mockMvc.perform(delete("/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(service, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete ShortUrl Should NotFoundException when ShortUrlId is not found")
    void deleteShortUrl_shouldReturn404_whenUrlNotFound() throws Exception {
        // Arrange
        String id = "nonExistentId";

        doThrow(new NotFoundException("Url Id nonExistentId not found")).when(service).deleteById(id);
        // Act & Assert
        mockMvc.perform(delete("/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).deleteById(id);
    }
}