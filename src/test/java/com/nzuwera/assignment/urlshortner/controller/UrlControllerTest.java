package com.nzuwera.assignment.urlshortner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nzuwera.assignment.urlshortner.entity.ShortUrl;
import com.nzuwera.assignment.urlshortner.model.CreateShortUrlRequest;
import com.nzuwera.assignment.urlshortner.model.DtoMapper;
import com.nzuwera.assignment.urlshortner.model.ResponseObject;
import com.nzuwera.assignment.urlshortner.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void UrlController_CreateShortUrl_ReturnCreatedResponseObject_001() throws Exception {
        shortUrl = DtoMapper.toEntity(request);
        responseObject.setStatus(true);
        responseObject.setCode(200);
        responseObject.setMessage(String.format("ShortUrl %s created successfully!", shortUrl.getUrlId()));
        responseObject.setData(shortUrl);
        when(service.create(request)).thenReturn(responseObject);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    @Test
    void UrlController_CreateShortUrl_ReturnCreatedResponseObject_002() throws Exception {
        request.setShortUrlId("GGL1234567");
        shortUrl = DtoMapper.toEntity(request);
        responseObject.setStatus(true);
        responseObject.setCode(200);
        responseObject.setMessage("ShortUrl GGL1234567 created successfully!");
        responseObject.setData(shortUrl);
        when(service.create(request)).thenReturn(responseObject);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.message").value("ShortUrl GGL1234567 created successfully!"));
    }
}