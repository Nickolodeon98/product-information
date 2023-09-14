package com.example.productinformation.parser;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RecommendParser implements Parser<Recommend> { // 연관 상품을 가공하는 클래스

  private final ProductRepository productRepository;

  @Override
  public Recommend parse(String line) {
    String[] row = line.split("\",\"");

    final int FIRST = 0;
    final int END = row.length - 1;

    row[FIRST] = row[FIRST].replaceAll("\"", "");
    row[END] = row[END].replaceAll("\"", "");

    Optional<Product> product = productRepository.findByItemId(Long.valueOf(row[FIRST]));
    Product relatedProduct = null;

    if (product.isEmpty()) {
      relatedProduct = Product.builder()
          .itemId(Long.valueOf(row[FIRST]))
          .build();
      productRepository.save(relatedProduct);
    } else relatedProduct = product.get();

    return Recommend.builder()
        .target(relatedProduct)
        .itemId(Long.valueOf(row[1]))
        .score(Integer.valueOf(row[2]))
        .ranking(Integer.valueOf(row[END]))
        .build();
  }
}
