package com.example.productinformation.domain.dto;

import com.example.productinformation.domain.entity.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TargetInfo {

  private Long itemId;
  private String itemName;
  private String itemImage;
  private String itemUrl;
  private Integer originalPrice;
  private Integer salePrice;

  public static List<TargetInfo> of(List<Product> products) {
    List<TargetInfo> targetInfos = new ArrayList<>();
    for (Product product : products) {
      TargetInfo targetInfo = TargetInfo.builder()
          .itemId(product.getItemId())
          .itemName(product.getItemName())
          .itemImage(product.getItemImage())
          .itemUrl(product.getItemUrl())
          .originalPrice(product.getOriginalPrice())
          .salePrice(product.getSalePrice())
          .build();

      targetInfos.add(targetInfo);
    }
    return targetInfos;
  }
}
