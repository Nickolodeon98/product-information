package com.example.productinformation.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.productinformation.domain.Product;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class ProductServiceTest {

  @Autowired
  ReadLineContext<Product> productReadLineContext;

  @Mock
  ProductRepository productRepository;

  @InjectMocks
  ProductService productService;

  String sampleLine1;
  String sampleLine2;
  String sampleLine3;

  @BeforeEach
  void setUp() {
    sampleLine1 = "\"300002285\",\"아비루즈 ha-15\",\"//image.wconcept.co.kr/productimg/image/img2/85/300002285.jpg\",\"m.wconcept.co.kr/product/300002285\",\"5900\",\"5900\"";
    sampleLine2 = "\"300002301\",\"아비루즈 bt-16\",\"//image.wconcept.co.kr/productimg/image/img2/01/300002301.jpg\",\"m.wconcept.co.kr/product/300002301\",\"5900\",\"5900\"";
    sampleLine3 = "\"300003606\",\"CANVAS TOTE BAG-BROWN\",\"//image.wconcept.co.kr/productimg/image/img2/06/300003606.jpg\",\"m.wconcept.co.kr/product/300003606\",\"98000\",\"98000\"";
  }
}