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
public class ProductInfo {

  private Long itemId;
  private String itemName;
  private String itemImage;
  private String itemUrl;
  private Integer originalPrice;
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
