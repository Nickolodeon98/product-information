package com.example.productinformation.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.fixture.ProductFixture;
import com.example.productinformation.fixture.RecommendFixture;
import com.example.productinformation.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
@Slf4j
class ItemControllerTest {

  @Autowired
  MockMvc mockMvc;
  @MockBean
  ItemService itemService;
  @Autowired
  ObjectMapper objectMapper;
  FileRequest fileRequest;
  List<Recommend> recommends;
  Recommend mockRecommend;
  Product item;
  Long itemId;
  final String url = "/rec/items/relevance";
  final String acquireUrl = "/rec";
  @BeforeEach
  void setUp() {
    fileRequest = FileRequest.builder()
        .filename("filename")
        .build();

    itemId = 300002285L;

    mockRecommend = RecommendFixture.get(itemId);
    recommends = new ArrayList<>();
    recommends.add(mockRecommend);

    item = ProductFixture.get(itemId);
  }

  @Nested
  @DisplayName("연관 상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공")
    void recommend_success() throws Exception {
      log.info("recommendsId:{}", recommends.get(0).getId());

      // given
      given(itemService.createRecommend(fileRequest))
          .willReturn(RecommendResponse.of(recommends, "등록 완료"));

      log.info("recommendResponse:{}", RecommendResponse.of(recommends, "등록 완료").getMessage());

      mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsBytes(fileRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.message").value("등록 완료"))
          .andDo(print());

      verify(itemService).createRecommend(fileRequest);
    }
  }

  @Nested
  @DisplayName("상품 조회")
  class ItemSearch {

    @Test
    @DisplayName("성공")
    void item_search_success() throws Exception {
      given(itemService.acquireItem(eq(itemId))).willReturn(ItemResponse.of(item, recommends));

      mockMvc.perform(get(acquireUrl)
              .param("id", String.valueOf(itemId)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.target[0].itemId").value(itemId))
          .andExpect(jsonPath("$.result.results[0].score").value(20))
          .andDo(print());

      verify(itemService).acquireItem(eq(itemId));
    }
  }

}