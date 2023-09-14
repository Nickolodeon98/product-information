package com.example.productinformation.domain.dto.response;

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
public class SingleRecommendResponse { // 새로운 연관 상품을 등록한 경우 API 응답을 담는 DTO
  private String message;
  @JsonProperty("target_item_id")
  private Long targetItemId;
  @JsonProperty("recommend_item_id")
  private Long recommendItemId;
  private Integer score;
  private Integer rank;

  public static SingleRecommendResponse of(Recommend recommendEntity, String message) {
    return SingleRecommendResponse.builder()
        .message(message)
        .targetItemId(recommendEntity.getTarget().getItemId())
        .recommendItemId(recommendEntity.getItemId())
        .score(recommendEntity.getScore())
        .rank(recommendEntity.getRanking())
        .build();
  }
}
