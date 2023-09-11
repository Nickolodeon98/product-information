package com.example.productinformation.service;

import com.example.productinformation.domain.Product;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ReadLineContext<Product> productReadLineContext;

  private final ProductRepository productRepository;


  public ProductResponse createProduct(String filename) throws IOException {

    List<Product> products = productRepository.saveAll(productReadLineContext.readLines(filename));

    return ProductResponse.of(products, "상품 등록 완료");
  }
}
