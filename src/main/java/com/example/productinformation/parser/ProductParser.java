package com.example.productinformation.parser;

import com.example.productinformation.domain.entity.Product;

public class ProductParser implements Parser<Product> {

  @Override
  public Product parse(String line) {
    String[] row = line.split("\",\"");

    final int FIRST = 0;
    final int END = row.length - 1;

    // 첫번째 컬럼과 마지막 컬럼은 변수에 넣기 전에 먼저 큰 따옴표(")를 제거해준다.
    row[FIRST] = row[FIRST].replaceAll("\"", "");
    row[END] = row[END].replaceAll("\"", "");

    // 빌더 패턴으로 Product 객체를 생성해 바로 리턴해준다.
    return Product.builder()
        .itemId(Long.valueOf(row[0]))
        .itemName(row[1])
        .itemImage(row[2])
        .itemUrl(row[3])
        .originalPrice(Integer.valueOf(row[4]))
        .salePrice(Integer.valueOf(row[5])).build();
  }
}
