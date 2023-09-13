package com.example.productinformation.exception;

import com.example.productinformation.domain.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ItemAppException {

  @ResponseBody
  @ExceptionHandler(ItemException.class)
  public ResponseEntity<?> itemExceptionHandler(ItemException exception) {
    ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
    return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
        .body(Response.fail(errorResponse));
  }

}
