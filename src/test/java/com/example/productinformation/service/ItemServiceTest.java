package com.example.productinformation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import com.example.productinformation.domain.dto.request.ProductEditRequest;
import com.example.productinformation.domain.dto.response.ProductEditResponse;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.dto.response.SingleRecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.exception.ErrorCode;
import com.example.productinformation.exception.ItemException;
import com.example.productinformation.fixture.ProductFixture;
import com.example.productinformation.fixture.RecommendFixture;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import com.example.productinformation.repository.RecommendRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ItemServiceTest {

  @Mock
  ReadLineContext<Product> productReadLineContext;

  @Mock
  ReadLineContext<Recommend> recommendReadLineContext;

  @Mock
  ProductRepository productRepository;

  @Mock
  RecommendRepository recommendRepository;

  @InjectMocks
  ItemService itemService;

  String sampleLine1;
  String sampleLine2;
  String sampleLine3;
  Product mockProduct;
  Recommend mockRecommend;

  List<Product> products;
  List<Recommend> recommends;
  FileRequest fileRequest;
  Long itemId;
  Long itemId2;
  Product wrongProduct;
  Recommend wrongRecommend;
  DetailedProductInfo recommendRequest;
  DetailedProductInfo wrongRecommendRequest;
  Product editedProduct;
  ProductEditRequest productEditRequest;
  @BeforeEach
  void setUp() {
    sampleLine1 = "\"300002285\",\"아비루즈 ha-15\",\"//image.wconcept.co.kr/productimg/image/img2/85/300002285.jpg\",\"m.wconcept.co.kr/product/300002285\",\"5900\",\"5900\"";
    sampleLine2 = "\"300002301\",\"아비루즈 bt-16\",\"//image.wconcept.co.kr/productimg/image/img2/01/300002301.jpg\",\"m.wconcept.co.kr/product/300002301\",\"5900\",\"5900\"";
    sampleLine3 = "\"300003606\",\"CANVAS TOTE BAG-BROWN\",\"//image.wconcept.co.kr/productimg/image/img2/06/300003606.jpg\",\"m.wconcept.co.kr/product/300003606\",\"98000\",\"98000\"";

    itemId = 300002285L;
    itemId2 = 300003606L;
    mockProduct = ProductFixture.get(itemId);

    mockRecommend = RecommendFixture.get(itemId);

    products = new ArrayList<>();
    products.add(mockProduct);

    recommends = new ArrayList<>();
    recommends.add(mockRecommend);
    fileRequest = FileRequest.builder().filename("filename").build();

    wrongProduct = ProductFixture.getWrong();
    wrongRecommend = RecommendFixture.getWrong(itemId);
    recommendRequest = DetailedProductInfo.of(mockProduct, mockRecommend);
    wrongRecommendRequest = DetailedProductInfo.of(wrongProduct, wrongRecommend);

    editedProduct = ProductFixture.get(itemId2);

    productEditRequest = ProductEditRequest.builder()
        .itemName("닥터마틴 블랙 스무스")
        .itemImage("//static.shoeprize.com/Raffle/thumb/11838002-shoeprize-Dr.-Martens-1461-Smooth-Leather-Oxford-Black-Smooth-NEW-1690913038337.jpg?f=webp&w=1000")
        .itemUrl("https://www.shoeprize.com/raffles/119061/")
        .originalPrice(210000)
        .salePrice(170000)
        .build();
  }

  //  @Nested
//  @DisplayName("상품 등록")
//  class ProductCreation {
//    @Test
//    @DisplayName("성공")
//    void success_create_product() throws IOException {
//      when(productReadLineContext.readLines("filename")).thenReturn(products);
//      when(productRepository.saveAll(any())).thenReturn(products);
//
//      ProductResponse response = itemService.createProduct(fileRequest);
//
//      Assertions.assertEquals(products.get(0).getId(), response.getProductIds().get(0));
//
//      verify(productRepository).saveAll(any());
//    }
//  }
  @Nested
  @DisplayName("상품 등록")
  class ProductCreation {

    @Test
    @DisplayName("성공")
    void success_create_product() throws IOException {
      when(productRepository.findByItemId(itemId)).thenReturn(Optional.empty());
      when(productRepository.save(any())).thenReturn(mockProduct);

      ProductInfo response = itemService.extraProduct(mockProduct.toRequest());

      Assertions.assertEquals(mockProduct.getItemId(), response.getItemId());

      verify(productRepository).findByItemId(itemId);
      verify(productRepository).save(any());
    }

    @Test
    @DisplayName("실패 - 상품의 고유 아이디가 주어지지 않음")
    void fail_create_product_no_id() {
      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.extraProduct(wrongProduct.toRequest()));

      Assertions.assertEquals(ErrorCode.INVALID_INPUT, e.getErrorCode());
    }

    @Test
    @DisplayName("실패 - 이미 존재하는 상품의 고유 아이디")
    void fail_create_product_duplicate_id() {
      when(productRepository.findByItemId(itemId)).thenReturn(Optional.of(mockProduct));

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.extraProduct(mockProduct.toRequest()));

      Assertions.assertEquals(ErrorCode.DUPLICATE_ITEM, e.getErrorCode());

      verify(productRepository).findByItemId(itemId);
    }
  }

//  @Nested
//  @DisplayName("연관 상품 등록")
//  class ProductRegistration {
//
//    @Test
//    @DisplayName("성공")
//    void success_add_product() throws IOException {
//      when(recommendReadLineContext.readLines("filename")).thenReturn(recommends);
//      when(recommendRepository.saveAll(any())).thenReturn(recommends);
//
//      RecommendResponse response = itemService.createRecommend(fileRequest);
//
//      Assertions.assertEquals(recommends.get(0).getId(), response.getRecommendIds().get(0));
//
//      verify(recommendRepository).saveAll(any());
//    }
//  }

  @Nested
  @DisplayName("연관 상품 등록")
  class RecommendRegistration {

    @Test
    @DisplayName("성공 - 연관 상품이 등록된 적이 없는 상품일 때")
    void success_add_recommend() throws IOException {
      lenient().when(recommendRepository.findByItemId((mockRecommend.getItemId()))).thenReturn(Optional.empty());
      lenient().when(productRepository.findByItemId(itemId)).thenReturn(Optional.of(mockProduct));

      lenient().when(productRepository.findByItemId(recommendRequest.getItemId())).thenReturn(Optional.empty());
      lenient().when(productRepository.save(recommendRequest.toProductEntity())).thenReturn(recommendRequest.toProductEntity());
      lenient().when(recommendRepository.save(any())).thenReturn(recommendRequest.toEntity(mockProduct));

      SingleRecommendResponse response = itemService.relateItems(recommendRequest, itemId);

      Assertions.assertEquals(mockRecommend.getItemId(), response.getRecommendItemId());
      Assertions.assertEquals(mockRecommend.getTarget().getItemId(), response.getTargetItemId());

      verify(productRepository).save(recommendRequest.toProductEntity());
      verify(recommendRepository).findByItemId(mockRecommend.getItemId());
      verify(productRepository).findByItemId(itemId);
      verify(productRepository).findByItemId(recommendRequest.getItemId());
      verify(recommendRepository).save(any());
    }
    @Test
    @DisplayName("실패 - 이미 존재하는 연관 상품")
    void fail_create_product_duplicate_id() {
      when(recommendRepository.findByItemId(recommendRequest.getItemId())).thenReturn(Optional.of(mockRecommend));

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.relateItems(recommendRequest, itemId));

      Assertions.assertEquals(ErrorCode.DUPLICATE_ITEM, e.getErrorCode());

      verify(recommendRepository).findByItemId(recommendRequest.getItemId());
    }
    @Test
    @DisplayName("실패 - 연관 상품의 고유 아이디가 주어지지 않음")
    void fail_add_recommend_id_not_found() throws IOException {
      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.relateItems(wrongRecommendRequest, itemId));

      Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, e.getErrorCode());
    }


    @Test
    @DisplayName("실패 - 존재하지 않는 대상 상품")
    void fail_add_recommend_target_not_found() {
      when(productRepository.findByItemId(itemId)).thenReturn(Optional.empty());

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.relateItems(recommendRequest, itemId));

      Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, e.getErrorCode());

      verify(productRepository).findByItemId(itemId);
    }


  }

  @Nested
  @DisplayName("상품 조회")
  class ItemSearch {

    @Test
    @DisplayName("성공 - 단건")
    void success_search_item() {
      when(productRepository.findByItemId(any())).thenReturn(Optional.of(mockProduct));
      when(recommendRepository.findAllByTarget(mockProduct)).thenReturn(recommends);

      Long id = recommends.get(0).getItemId();
      Long secondId = itemService.acquireItem(String.valueOf(itemId)).getResults().get(0)
          .getItemId();

      Assertions.assertEquals(id, secondId);

      verify(productRepository, times(2)).findByItemId(any());
      verify(recommendRepository).findAllByTarget(mockProduct);
    }

    @Test
    @DisplayName("성공 - 2건 이상")
    void success_search_several_item() {
      when(productRepository.findAllByItemIdIn(any())).thenReturn(Optional.of(products));
      when(recommendRepository.findAllByTargetIn(products)).thenReturn(recommends);
      when(productRepository.findByItemId(any())).thenReturn(Optional.of(mockProduct));

      Assertions.assertEquals(recommends.get(0).getItemId(),
          itemService.acquireItem("300373871,300373871").getResults().get(0).getItemId());

      verify(productRepository).findByItemId(any());
      verify(productRepository).findAllByItemIdIn(any());
      verify(recommendRepository).findAllByTargetIn(products);
    }

    @Test
    @DisplayName("실패 - 단건")
    void fail_search_item() {
      when(productRepository.findByItemId(any())).thenReturn(Optional.empty());

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.acquireItem(String.valueOf(itemId)));

      Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, e.getErrorCode());

      verify(productRepository).findByItemId(any());
    }

    @Test
    @DisplayName("실패 - 2건 이상")
    void fail_search_several_item() {
      when(productRepository.findAllByItemIdIn(any())).thenReturn(Optional.empty());

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.acquireItem("300002285,300005968"));

      Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, e.getErrorCode());

      verify(productRepository).findAllByItemIdIn(any());
    }
  }

  @Nested
  @DisplayName("상품 수정")
  class ItemEdition {

    @Test
    @DisplayName("성공")
    void success_edit_product() {
      when(productRepository.findByItemId(any())).thenReturn(Optional.of(mockProduct));
      when(productRepository.save(any())).thenReturn(editedProduct);

      ProductEditResponse response = itemService.editProduct(String.valueOf(itemId), productEditRequest);

      Assertions.assertEquals(editedProduct.getItemId(), response.getEditedItemId());

      verify(productRepository).findByItemId(any());
      verify(productRepository).save(any());
    }

    @Test
    @DisplayName("실패 - 상품의 입력이 올바르게 주어지지 않음")
    void fail_add_recommend_id_not_found() throws IOException {
      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.editProduct(" ", ProductEditRequest.of(editedProduct)));

      Assertions.assertEquals(ErrorCode.INVALID_INPUT, e.getErrorCode());
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 대상 상품")
    void fail_edit_product() {
      when(productRepository.findByItemId(any())).thenReturn(Optional.empty());

      ItemException e = Assertions.assertThrows(ItemException.class,
          () -> itemService.editProduct(String.valueOf(itemId), ProductEditRequest.of(editedProduct)));

      Assertions.assertEquals(ErrorCode.ITEM_NOT_FOUND, e.getErrorCode());

      verify(productRepository).findByItemId(any());
    }
  }
}