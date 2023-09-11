package com.example.productinformation.domain;

import com.example.productinformation.domain.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
  private String resultCode;
  private T result;
  public static <T> Response<T> success(T result) {
    return new Response<>("SUCCESS", result);
  }
}
