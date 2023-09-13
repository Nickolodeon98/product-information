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
public class RecommendParser implements Parser<Recommend> {

  private final ProductRepository productRepository;

  @Override
  public Recommend parse(String line) {
    String[] row = line.split("\",\"");

    final int FIRST = 0;
    final int END = row.length - 1;

    row[FIRST] = row[FIRST].replaceAll("\"", "");
    row[END] = row[END].replaceAll("\"", "");

    Product product = productRepository.findByItemId(Long.valueOf(row[FIRST]))
        .orElseThrow(() -> new IllegalArgumentException());

    return Recommend.builder()
        .target(product)
        .itemId(Long.valueOf(row[1]))
        .score(Integer.valueOf(row[2]))
        .ranking(Integer.valueOf(row[END]))
        .build();
  }
}
