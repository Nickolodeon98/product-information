package com.example.productinformation.domain.dto.response;

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
public class ProductDeleteResponse {

  @JsonProperty("deleted_item_id")
  private Long deletedItemId;
  private String message;


  public static ProductDeleteResponse of(Product deletedProduct, String message) {
    return ProductDeleteResponse.builder()
        .deletedItemId(deletedProduct.getItemId())
        .message(message)
        .build();

  }
}
