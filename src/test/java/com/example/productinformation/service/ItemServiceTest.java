package com.example.productinformation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import com.example.productinformation.repository.RecommendRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
// @ExtendWith 를 활용해 ...
@ExtendWith({MockitoExtension.class, SpringExtension.class})
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
  @BeforeEach
  void setUp() {
    sampleLine1 = "\"300002285\",\"아비루즈 ha-15\",\"//image.wconcept.co.kr/productimg/image/img2/85/300002285.jpg\",\"m.wconcept.co.kr/product/300002285\",\"5900\",\"5900\"";
    sampleLine2 = "\"300002301\",\"아비루즈 bt-16\",\"//image.wconcept.co.kr/productimg/image/img2/01/300002301.jpg\",\"m.wconcept.co.kr/product/300002301\",\"5900\",\"5900\"";
    sampleLine3 = "\"300003606\",\"CANVAS TOTE BAG-BROWN\",\"//image.wconcept.co.kr/productimg/image/img2/06/300003606.jpg\",\"m.wconcept.co.kr/product/300003606\",\"98000\",\"98000\"";

    mockProduct = Product.builder().itemId(300002285L).itemName("아비루즈 ha-15")
        .itemImage("//image.wconcept.co.kr/productimg/image/img2/85/300002285.jpg")
        .itemUrl("m.wconcept.co.kr/product/300002285").originalPrice(5900).salePrice(5900).build();

    mockRecommend = Recommend.builder()
        .id(1L)
        .targetItem(mockProduct)
        .itemId(300373871L)
        .score(20)
        .rank(1)
        .build();

    products = new ArrayList<>();
    products.add(mockProduct);

    recommends = new ArrayList<>();
    recommends.add(mockRecommend);
    fileRequest = FileRequest.builder().filename("filename").build();
  }

  @Nested
  @DisplayName("상품 등록")
  class ProductCreation {

    @Test
    @DisplayName("성공")
    void success_create_product() throws IOException {
      when(productReadLineContext.readLines("filename")).thenReturn(products);
      when(productRepository.saveAll(any())).thenReturn(products);

      ProductResponse response = itemService.createProduct(fileRequest);

      Assertions.assertEquals(products.get(0).getId(), response.getProductIds().get(0));

      verify(productRepository).saveAll(any());
    }
  }

  @Nested
  @DisplayName("연관 상품 등록")
  class ProductAcquisition {

    @Test
    @DisplayName("성공")
    void success_read_product() throws IOException {
      when(recommendReadLineContext.readLines("filename")).thenReturn(recommends);
      when(recommendRepository.saveAll(any())).thenReturn(recommends);

      RecommendResponse response = itemService.createRecommend(fileRequest);

      Assertions.assertEquals(recommends.get(0).getId(), response.getRecommendIds().get(0));

      verify(recommendRepository).saveAll(any());
    }

    @Test
    @DisplayName("실패")
    void fail_read_product() {

    }
  }
}