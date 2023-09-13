package com.example.productinformation.domain.dto;

import com.example.productinformation.domain.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductInfo {

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

  public static ProductInfo of(Product product) {
    return ProductInfo.builder()
        .itemId(product.getItemId())
        .itemName(product.getItemName())
        .itemImage(product.getItemImage())
        .itemUrl(product.getItemUrl())
        .originalPrice(product.getOriginalPrice())
        .salePrice(product.getSalePrice())
        .build();
  }

  public static List<ProductInfo> of(List<Product> products) {
    List<ProductInfo> productInfos = new ArrayList<>();
    for (Product product : products) {
      productInfos.add(ProductInfo.of(product));
    }
    return productInfos;
  }

  public Product toEntity() {
    return Product.builder()
        .itemId(this.itemId)
        .itemName(this.itemName)
        .itemImage(this.itemImage)
        .itemUrl(this.itemUrl)
        .originalPrice(this.originalPrice)
        .salePrice(this.salePrice)
        .build();
  }
}
