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
public class ProductEditResponse {
  @JsonProperty("edited_item_id")
  private Long editedItemId;
  private String message;

  public static ProductEditResponse of(Product editedProduct, String message) {
    return ProductEditResponse.builder()
        .editedItemId(editedProduct.getItemId())
        .message(message)
        .build();
  }
}
