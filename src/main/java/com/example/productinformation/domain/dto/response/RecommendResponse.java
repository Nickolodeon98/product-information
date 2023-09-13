package com.example.productinformation.domain.dto.response;

import com.example.productinformation.domain.entity.Recommend;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecommendResponse {
  List<Long> recommendIds;
  String message;

  public static RecommendResponse of(List<Recommend> recommends, String message) {
    List<Long> recommendIds = new ArrayList<>();

    for (Recommend recommend : recommends) {
      recommendIds.add(recommend.getId());
    }

    return RecommendResponse.builder()
        .recommendIds(recommendIds)
        .message(message)
        .build();
  }
}
