package com.example.productinformation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemException extends RuntimeException {
  private ErrorCode errorCode;
  private String message;

  public ItemException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }
}
