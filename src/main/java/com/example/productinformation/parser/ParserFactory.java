package com.example.productinformation.parser;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ParserFactory { // 다양한 종류의 parser 를 담는 클래스
  private final ProductRepository productRepository;
  @Bean
  public ReadLineContext<Product> productReadLineContext() {
    return new ReadLineContext<Product>(new ProductParser());
  }

  @Bean
  public ReadLineContext<Recommend> recommendReadLineContext() {
    return new ReadLineContext<Recommend>(new RecommendParser(productRepository));
  }
}
