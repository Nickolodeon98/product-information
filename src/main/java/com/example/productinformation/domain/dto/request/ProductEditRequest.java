package com.example.productinformation.domain.dto.request;

import com.example.productinformation.domain.entity.Product;
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
public class ProductEditRequest {

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


  public static ProductEditRequest of(Product product) {
    return ProductEditRequest.builder()
        .itemName(product.getItemName())
        .itemImage(product.getItemImage())
        .itemUrl(product.getItemUrl())
        .originalPrice(product.getOriginalPrice())
        .salePrice(product.getSalePrice())
        .build();
  }

  public Product toEntity(Long id, Long itemId) {
    return Product.builder()
        .id(id)
        .itemId(itemId)
        .itemName(this.itemName)
        .itemImage(this.itemImage)
        .itemUrl(this.itemUrl)
        .originalPrice(this.originalPrice)
        .salePrice(this.salePrice)
        .build();
  }
}
