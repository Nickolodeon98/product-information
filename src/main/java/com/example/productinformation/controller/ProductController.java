package com.example.productinformation.controller;

import com.example.productinformation.domain.dto.request.ProductRequest;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.Response;
import com.example.productinformation.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Api(tags="상품 CRUD 엔드포인트")
@ApiOperation("상품 관련 API 의 컨트롤러")
public class ProductController {

  private final ProductService productService;

  @ResponseBody
  @Operation(summary="상품 등록", description = "주어지는 파일 이름으로부터 상품 데이터를 추출하여 DB에 저장할 수 있다.")
  @PostMapping("/new")
  public Response<ProductResponse> registerItems(@RequestBody ProductRequest productRequest)
      throws IOException {
    ProductResponse productResponse = productService.createProduct(productRequest);

    return Response.success(productResponse);
  }
}
