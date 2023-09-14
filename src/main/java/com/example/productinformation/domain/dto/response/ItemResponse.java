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

  private List<ProductInfo> target;
  private List<DetailedProductInfo> results;

  public static ItemResponse of(List<ProductInfo> products, List<DetailedProductInfo> detailedProductInfos) {
    return ItemResponse.builder()
        .target(products)
        .results(detailedProductInfos)
        .build();
  }
}
