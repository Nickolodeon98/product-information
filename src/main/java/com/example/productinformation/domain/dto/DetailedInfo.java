package com.example.productinformation.domain.dto;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedInfo {

  private Long itemId;
  private String itemName;
  private String itemImage;
  private String itemUrl;
  private Integer originalPrice;
  private Integer salePrice;
  private Integer score;
  private Integer ranking;

  public static DetailedInfo of(Product productInfo, Recommend recommend) {
    return DetailedInfo.builder()
        .itemId(recommend.getItemId())
        .itemName(productInfo.getItemName() == null ? null : productInfo.getItemName())
        .itemImage(productInfo.getItemImage() == null ? null : productInfo.getItemImage())
        .itemUrl(productInfo.getItemUrl() == null ? null : productInfo.getItemUrl())
        .originalPrice(productInfo.getOriginalPrice() == null ? null : productInfo.getOriginalPrice())
        .salePrice(productInfo.getSalePrice() == null ? null : productInfo.getSalePrice())
        .score(recommend.getScore())
        .ranking(recommend.getRanking())
        .build();
  }
}
