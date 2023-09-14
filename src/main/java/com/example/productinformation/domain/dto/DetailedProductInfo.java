package com.example.productinformation.domain.dto;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class DetailedProductInfo { // 상품 정보에 연관도 정보까지 합쳐진 연관 상품의 상세 정보

  @JsonProperty("item_id")
  private Long itemId;
  @JsonProperty("item_name")
  private String itemName;
  @JsonProperty("item_image")
  private String itemImage;
  @JsonProperty("item_url")
  private String itemUrl;
  @JsonProperty("original_price")
  private Integer originalPrice;
  @JsonProperty("sale_price")
  private Integer salePrice;
  @JsonProperty("score")
  private Integer score;
  @JsonProperty("rank")
  private Integer rank;

  public static DetailedProductInfo of(Product productInfo, Recommend recommend) {
    return DetailedProductInfo.builder()
        .itemId(recommend.getItemId() == null ? null : recommend.getItemId())
        .itemName(productInfo.getItemName() == null ? null : productInfo.getItemName())
        .itemImage(productInfo.getItemImage() == null ? null : productInfo.getItemImage())
        .itemUrl(productInfo.getItemUrl() == null ? null : productInfo.getItemUrl())
        .originalPrice(productInfo.getOriginalPrice() == null ? null : productInfo.getOriginalPrice())
        .salePrice(productInfo.getSalePrice() == null ? null : productInfo.getSalePrice())
        .score(recommend.getScore())
        .rank(recommend.getRanking())
        .build();
  }

  public Product toProductEntity() {
    return Product.builder()
        .itemId(this.itemId)
        .itemName(this.itemName)
        .itemImage(this.itemImage)
        .itemUrl(this.itemUrl)
        .originalPrice(this.originalPrice)
        .salePrice(this.salePrice)
        .build();
  }

  public Recommend toEntity(Product product) {
    return Recommend.builder()
        .itemId(this.itemId)
        .target(product)
        .score(this.score)
        .ranking(this.rank)
        .build();
  }
}
