package com.example.productinformation.fixture;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;

public class RecommendFixture {

  public static Recommend get(Long itemId) {
    Product product = ProductFixture.get(itemId);

    return Recommend.builder()
        .target(product)
        .id(1L)
        .itemId(300373871L)
        .score(20)
        .ranking(1)
        .build();
  }

  public static Recommend getWrong(Long itemId) {
    Product product = ProductFixture.get(itemId);

    return Recommend.builder()
        .target(product)
        .id(1L)
        .score(20)
        .ranking(1)
        .build();
  }
}
