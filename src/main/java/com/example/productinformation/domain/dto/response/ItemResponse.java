package com.example.productinformation.domain.dto.response;

import com.example.productinformation.domain.dto.DetailedInfo;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
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

  private List<Product> target;
  private List<DetailedInfo> results;

  public static ItemResponse of(List<Product> products, List<DetailedInfo> detailedInfos) {
    return ItemResponse.builder()
        .target(products)
        .results(detailedInfos)
        .build();
  }
}
