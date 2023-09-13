package com.example.productinformation.fixture;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import java.util.ArrayList;
import java.util.List;

public class ProductFixture {
  public static Product get(Long itemId) {
    List<Recommend> recommends = new ArrayList<>();

    return Product.builder().id(1L).itemId(itemId).itemName("아비루즈 ha-15")
        .itemImage("//image.wconcept.co.kr/productimg/image/img2/85/300002285.jpg")
        .itemUrl("m.wconcept.co.kr/product/300002285").originalPrice(5900).salePrice(5900).build();
  }
}
