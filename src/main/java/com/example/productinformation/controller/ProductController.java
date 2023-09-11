package com.example.productinformation.controller;

import com.example.productinformation.domain.dto.request.ProductRequest;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.Response;
import com.example.productinformation.service.ProductService;
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
public class ProductController {

  private final ProductService productService;

  @ResponseBody
  @PostMapping("/new")
  public Response<ProductResponse> registerItems(@RequestBody ProductRequest productRequest)
      throws IOException {
    ProductResponse productResponse = productService.createProduct(productRequest);

    return Response.success(productResponse);
  }
}
