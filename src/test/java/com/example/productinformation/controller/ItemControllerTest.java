package com.example.productinformation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.request.ProductEditRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.ProductEditResponse;
import com.example.productinformation.domain.dto.response.SingleRecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.exception.ErrorCode;
import com.example.productinformation.exception.ItemException;
import com.example.productinformation.fixture.ProductFixture;
import com.example.productinformation.fixture.RecommendFixture;
import com.example.productinformation.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
  ProductInfo productRequest;
  ProductEditRequest productEditRequest;
  Product mockItem;
  List<Product> products;
  List<Recommend> recommends;
  List<DetailedProductInfo> detailedProductInfos;
  Recommend mockRecommend;
  Long itemId;
  String severalIds;
  final String url = "/rec/items/relevance";
  final String acquireUrl = "/rec";
  final String extraUrl = "/rec/items/extra";
  final String extraRecommendUrl = "/rec/items/chain";

  final String editionUrl = "/rec/items/update";
  final String deletionUrl = "/rec/items/removal"

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

    detailedProductInfos = new ArrayList<>();
    detailedProductInfos.add(DetailedProductInfo.of(mockItem, mockRecommend));

    productEditRequest = ProductEditRequest.builder()
        .itemName("닥터마틴 블랙 스무스")
        .itemImage(
            "//static.shoeprize.com/Raffle/thumb/11838002-shoeprize-Dr.-Martens-1461-Smooth-Leather-Oxford-Black-Smooth-NEW-1690913038337.jpg?f=webp&w=1000")
        .itemUrl("https://www.shoeprize.com/raffles/119061/")
        .originalPrice(210000)
        .salePrice(170000)
        .build();
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
  class ProductRegistration {

    @Test
    @DisplayName("성공")
    void success_register_product() throws Exception {
      // given
      given(itemService.extraProduct(any()))
          .willReturn(ProductInfo.of(mockItem));

      mockMvc.perform(post(extraUrl).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(productRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.itemId").value(itemId))
          .andDo(print());

      verify(itemService).extraProduct(any());
    }

    @Test
    @DisplayName("실패")
    void fail_register_product() throws Exception {
      given(itemService.extraProduct(any()))
          .willThrow(
              new ItemException(ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getMessage()));

      mockMvc.perform(post(extraUrl).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(any())))
          .andExpect(status().isNotAcceptable())
          .andExpect(jsonPath("$.resultCode").value("ERROR"))
          .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_INPUT.name()))
          .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_INPUT.getMessage()))
          .andDo(print());

      verify(itemService).extraProduct(any());
    }
  }

  @Nested
  @DisplayName("연관 상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공")
    void success_register_recommend() throws Exception {
      // given
      given(itemService.relateItems(any(), any()))
          .willReturn(SingleRecommendResponse.of(mockRecommend, "등록 완료"));

      mockMvc.perform(post(extraRecommendUrl).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(any()))
              .content(objectMapper.writeValueAsBytes(any())))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.target_item_id").value(itemId))
          .andExpect(jsonPath("$.result.message").value("등록 완료"))
          .andDo(print());

      verify(itemService).relateItems(any(), any());
    }

    @Test
    @DisplayName("실패")
    void fail_register_product() throws Exception {
      given(itemService.relateItems(any(), any()))
          .willThrow(
              new ItemException(ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getMessage()));

      mockMvc.perform(post(extraRecommendUrl).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(any()))
              .content(objectMapper.writeValueAsBytes(any())))
          .andExpect(status().isNotAcceptable())
          .andExpect(jsonPath("$.resultCode").value("ERROR"))
          .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.INVALID_INPUT.name()))
          .andExpect(jsonPath("$.result.message").value(ErrorCode.INVALID_INPUT.getMessage()))
          .andDo(print());

      verify(itemService).relateItems(any(), any());
    }
  }

  @Nested
  @DisplayName("상품 조회")
  class ItemSearch {

    @Test
    @DisplayName("성공 - 단건")
    void item_search_success() throws Exception {

      given(itemService.acquireItem(String.valueOf(itemId))).willReturn(
          ItemResponse.of(ProductInfo.of(products),
              detailedProductInfos));

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

      given(itemService.acquireItem(severalIds)).willReturn(
          ItemResponse.of(ProductInfo.of(products),
              detailedProductInfos));

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

  @Nested
  @DisplayName("상품 정보 수정")
  class ItemModification {

    @Test
    @DisplayName("성공")
    void success_modify_item() throws Exception {
      given(itemService.editProduct(any(), any())).willReturn(
          ProductEditResponse.of(mockItem, "수정 완료"));

      mockMvc.perform(put(editionUrl)
              .param("itemId", String.valueOf(itemId))
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(productEditRequest)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.message").value("수정 완료"))
          .andExpect(jsonPath("$.result.edited_item_id").value(mockItem.getItemId()))
          .andDo(print());

      verify(itemService).editProduct(any(), any());
    }

    @Test
    @DisplayName("실패 - 상품 없음")
    void fail_modify_item() throws Exception {
      given(itemService.editProduct(any(), any()))
          .willThrow(new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage()));

      mockMvc.perform(put(editionUrl)
              .param("itemId", String.valueOf(itemId))
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsBytes(productEditRequest)))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.ITEM_NOT_FOUND.name()))
          .andExpect(jsonPath("$.result.message").value(ErrorCode.ITEM_NOT_FOUND.getMessage()))
          .andDo(print());

      verify(itemService).editProduct(any(), any());
    }
  }

  @Nested
  @DisplayName("상품 삭제")
  class ItemRemoval {
    @Test
    @DisplayName("성공")
    void success_delete_product() throws Exception {

      given(itemService.removeProduct(any()))
          .willReturn(ProductDeleteResponse.of(mockItem, "상품 삭제 완료"));

      mockMvc.perform(delete(deletionUrl)
              .param("itemId", String.valueOf(itemId)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
          .andExpect(jsonPath("$.result.message").value("상품 삭제 완료"))
          .andExpect(jsonPath("$.result.deleted_item_id").value(mockItem))
          .andDo(print());

      verify(itemService).removeProduct(any());
    }

    @Test
    @DisplayName("실패 - 삭제하고자 하는 상품 없음")
    void fail_delete_product() throws Exception {
      given(itemService.removeProduct(any()))
          .willThrow(new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage()));

      mockMvc.perform(delete(deletionUrl)
              .param("itemId", String.valueOf(itemId)))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.result.errorCode").value(ErrorCode.ITEM_NOT_FOUND.name()))
          .andExpect(jsonPath("$.result.message").value(ErrorCode.ITEM_NOT_FOUND.getMessage()))
          .andDo(print());

      verify(itemService).removeProduct(any());
    }
  }

}