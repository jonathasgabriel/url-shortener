package com.example.urlshortener.api.controller;

import com.example.urlshortener.domain.config.ApplicationConfig;
import com.example.urlshortener.domain.dto.CreateUrlDto;
import com.example.urlshortener.domain.dto.UrlDto;
import com.example.urlshortener.domain.model.Url;
import com.example.urlshortener.domain.service.UrlCreationService;
import com.example.urlshortener.domain.service.UrlRetrievalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
class UrlControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UrlCreationService creationService;

  @MockBean
  private UrlRetrievalService retrievalService;

  @MockBean
  private ApplicationConfig applicationConfig;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnAppUrlLinkWhenCreatingUrl() throws Exception {
    //given
    String baseUrl = "http://localhost/";
    String originalUrl = "http://original-very-long-url";
    CreateUrlDto requestBody = new CreateUrlDto(originalUrl);
    int expectedResponseCode = 201;
    String shortUrl = "short-url";

    Url url = Url.builder().shortUrl(shortUrl).build();

    when(creationService.create(any(CreateUrlDto.class))).thenReturn(url);
    when(applicationConfig.getBaseUrl()).thenReturn(baseUrl);

    // then
    MvcResult result = this.mockMvc.perform(
                    post("/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestBody)))
            .andExpect(status().is(expectedResponseCode))
            .andExpect(jsonPath("$.url").isString())
            .andReturn();

    String responseBody = result.getResponse().getContentAsString();
    UrlDto returnedDto = objectMapper.readValue(responseBody, UrlDto.class);

    assertTrue(returnedDto.getUrl().startsWith(baseUrl));
  }

  @Test
  void shouldRedirectUserToOriginalUrlWhenAccessingUrl() throws Exception {
    //given
    String originalUrl = "http://original-very-long-url";
    int expectedResponseCode = 303;
    String shortUrl = "short-url";

    Url url = Url.builder().originalUrl(originalUrl).build();

    when(retrievalService.retrieveOriginalUrl(shortUrl)).thenReturn(originalUrl);

    // then
    this.mockMvc.perform(
                    get("/" + shortUrl)
            )
            .andExpect(status().is(expectedResponseCode));
  }

}
