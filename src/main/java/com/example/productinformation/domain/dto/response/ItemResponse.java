package com.example.productinformation.domain.dto.response;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemResponse {

  private List<ProductInfo> target; // 조회 대상이 되는 상품
  private List<DetailedProductInfo> results; // 조회 대상 상품의 연관 상품들

  public static ItemResponse of(List<ProductInfo> products, List<DetailedProductInfo> detailedProductInfos) {
    return ItemResponse.builder()
        .target(products)
        .results(detailedProductInfos)
        .build();
  }
}
