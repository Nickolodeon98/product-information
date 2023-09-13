package com.example.productinformation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
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

  final String url = "/items/relevance";

  @BeforeEach
  void setUp() {
    fileRequest = FileRequest.builder()
        .filename("filename")
        .build();
    mockRecommend = Recommend.builder()
        .id(1L)
        .itemId(300373871L)
        .target(Product.builder().build())
        .score(20)
        .ranking(1)
        .build();

    recommends = new ArrayList<>();
    recommends.add(mockRecommend);
  }

  @Nested
  @DisplayName("연관 상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공")
    public void recommend_success() throws Exception {
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

}