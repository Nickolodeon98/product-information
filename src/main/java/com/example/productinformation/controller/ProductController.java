package com.example.productinformation.controller;

import com.example.productinformation.domain.dto.ProductResponse;
import com.example.productinformation.service.ProductService;
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
public class ProductController {

  private final ProductService productService;

  @ResponseBody
  @PostMapping("/new")
  public Response<ProductResponse> registerItems(@RequestBody ProductRequest productRequest) {
    ProductResponse productResponse = productService.createProduct(productRequest);

    return Response.success(productResponse);
  }
}
