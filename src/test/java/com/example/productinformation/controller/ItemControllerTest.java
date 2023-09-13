package com.example.productinformation.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productinformation.domain.dto.DetailedInfo;
import com.example.productinformation.domain.dto.TargetInfo;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
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
  ProductRequest productRequest;
  Product mockItem;
  List<Product> products;
  List<Recommend> recommends;
  List<DetailedInfo> detailedInfos;
  Recommend mockRecommend;
  Long itemId;
  String severalIds;
  final String url = "/rec/items/relevance";
  final String acquireUrl = "/rec";
  final String extraUrl = "/rec/items/extra";
  @BeforeEach
  void setUp() {
    fileRequest = FileRequest.builder()
        .filename("filename")
        .build();

    itemId = 300002285L;
    severalIds = "300002285,300002301,300003606";

    mockRecommend = RecommendFixture.get(itemId);
    recommends = new ArrayList<>();
    recommends.add(mockRecommend);

    mockItem = ProductFixture.get(itemId);
    products = new ArrayList<>();
    products.add(mockItem);

    detailedInfos = new ArrayList<>();
    detailedInfos.add(DetailedInfo.of(mockItem,mockRecommend));
  }

//  @Nested
//  @DisplayName("연관 상품 등록")
//  class RecommendRegistration {
//
//    @Test
//    @DisplayName("성공")
//    void recommend_success() throws Exception {
//      log.info("recommendsId:{}", recommends.get(0).getId());
//
//      // given
//      given(itemService.createRecommend(fileRequest))
//          .willReturn(RecommendResponse.of(recommends, "등록 완료"));
//
//      log.info("recommendResponse:{}", RecommendResponse.of(recommends, "등록 완료").getMessage());
//
//      mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
//          .content(objectMapper.writeValueAsBytes(fileRequest)))
//          .andExpect(status().isOk())
//          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
//          .andExpect(jsonPath("$.result.message").value("등록 완료"))
//          .andDo(print());
//
//      verify(itemService).createRecommend(fileRequest);
//    }
//  }

  @Nested
  @DisplayName("상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공")
    void recommend_success() throws Exception {
      // given
      given(itemService.extraProduct(productRequest))
          .willReturn(TargetInfo.of(mockItem));

      mockMvc.perform(post(extraUrl).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(productRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.itemId").value(itemId))
          .andDo(print());

      verify(itemService).extraProduct(productRequest);
    }
  }

  @Nested
  @DisplayName("상품 조회")
  class ItemSearch {

    @Test
    @DisplayName("성공 - 단건")
    void item_search_success() throws Exception {

      given(itemService.acquireItem(String.valueOf(itemId))).willReturn(ItemResponse.of(TargetInfo.of(products), detailedInfos));

      mockMvc.perform(get(acquireUrl)
              .param("id", String.valueOf(itemId)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.target[0].itemId").value(itemId))
          .andExpect(jsonPath("$.result.results[0].score").value(20))
          .andDo(print());

      verify(itemService).acquireItem(String.valueOf(itemId));
    }


    @Test
    @DisplayName("성공 - 다수")
    void several_item_search_success() throws Exception {
      Product product2 = ProductFixture.get(300002301L);
      Product product3 = ProductFixture.get(300003606L);

      products.add(product2);
      products.add(product3);

      given(itemService.acquireItem(severalIds)).willReturn(ItemResponse.of(TargetInfo.of(products), detailedInfos));

      mockMvc.perform(get(acquireUrl)
              .param("id", severalIds))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.target[0].itemId").value(itemId))
          .andExpect(jsonPath("$.result.results[0].score").value(20))
          .andDo(print());

      verify(itemService).acquireItem(severalIds);
    }

  }

}