package com.example.productinformation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품이 없습니다."),
  INVALID_INPUT(HttpStatus.NOT_ACCEPTABLE, "올바르지 않은 입력입니다."),
  DUPLICATE_ITEM(HttpStatus.CONFLICT, "item_id 가 중복됩니다.");

  private final HttpStatus httpStatus;
  private final String message;

  ErrorCode(HttpStatus httpStatus, String message) {
    this.httpStatus = httpStatus;
    this.message = message;
  }
}
