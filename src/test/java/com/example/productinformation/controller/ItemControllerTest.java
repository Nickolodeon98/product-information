package com.example.productinformation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ItemService itemService;

  @Autowired
  ObjectMapper objectMapper;

  FileRequest fileRequest;

  List<Recommend> recommends;

  final String url = "/item/relevance";

  @BeforeEach
  void setUp() {
    fileRequest = FileRequest.builder()
        .filename("filename")
        .build();
  }

  @Nested
  @DisplayName("연관 상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공")
    public void recommend_success() throws Exception {
      given(itemService.createRecommend(fileRequest))
          .willReturn(RecommendResponse.of(recommends, "등록 완료"));

      mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsBytes(fileRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.message").value("등록 완료"))
          .andDo(print());

      verify(itemService).createRecommend(fileRequest);
    }
  }

}