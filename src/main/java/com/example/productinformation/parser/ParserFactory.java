package com.example.productinformation.parser;

import com.example.productinformation.domain.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserFactory {

  @Bean
  public ReadLineContext<Product> productReadLineContext() {

  }
}
