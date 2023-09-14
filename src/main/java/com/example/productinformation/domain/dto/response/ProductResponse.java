package com.example.productinformation.domain.dto.response;

import com.example.productinformation.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ProductResponse {
  private List<Long> productIds; // csv 파일로부터 읽어서 저장한 상품들의 아이디들
  private String message;
  public static ProductResponse of(List<Product> products, String message) {
    List<Long> ids = new ArrayList<>();
    for (Product product : products) {
      ids.add(product.getId());
    }

    return ProductResponse.builder()
        .productIds(ids)
        .message(message)
        .build();
  }
}
