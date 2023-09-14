package com.example.productinformation.controller;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.Response;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.dto.response.SingleRecommendResponse;
import com.example.productinformation.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rec")
@RequiredArgsConstructor
@Slf4j
@Api(tags = "상품 CRUD 엔드포인트")
@ApiOperation("상품 관련 API 의 컨트롤러")
public class ItemController {

  private final ItemService itemService;

  @ResponseBody
  @Operation(summary = "[관리자] 상품 등록", description = "입력된 파일 이름으로부터 상품 데이터를 추출하여 DB에 저장할 수 있다.")
  @PostMapping("/items/new")
  public Response<ProductResponse> registerItems(@RequestBody FileRequest fileRequest)
      throws IOException {
    ProductResponse productResponse = itemService.createProduct(fileRequest);

    return Response.success(productResponse);
  }

  @ResponseBody
  @Operation(summary = "[관리자] 연관 상품 등록", description = "입력된 파일 이름으로부터 연관 상품 데이터를 추출하여 DB에 저장할 수 있다.")
  @PostMapping("/items/relevance")
  public Response<RecommendResponse> registerRecommends(@RequestBody FileRequest fileRequest)
      throws IOException {

    RecommendResponse recommendResponse = itemService.createRecommend(fileRequest);

    return Response.success(recommendResponse);
  }

  @ResponseBody
  @Operation(summary = "상품 조회", description = "상품 정보 및 연관 상품에 대해 조회할 수 있다.")
  @GetMapping
  public Response<ItemResponse> searchItems(@RequestParam("id") String itemId) {

    // itemId 를 Long 으로 변환해준다.
    ItemResponse itemResponse = itemService.acquireItem(itemId);

    return Response.success(itemResponse);
  }

  @ResponseBody
  @Operation(summary = "[사용자] 상품 등록", description = "입력된 상품 정보를 토대로 DB 에 상품을 저장할 수 있다.")
  @PostMapping("/items/extra")
  public Response<ProductInfo> registerItem(
      @RequestBody(required = false) ProductInfo productRequest) {

    ProductInfo response = itemService.extraProduct(productRequest);

    return Response.success(response);
  }

  @ResponseBody
  @Operation(summary = "[사용자] 연관 상품 등록", description = "입력된 연관 상품 정보를 토대로 DB 에 연관 상품을 저장할 수 있다.")
  @PostMapping("/items/chain")
  public Response<SingleRecommendResponse> registerRecommend(
      @RequestBody(required = false) DetailedProductInfo recommendRequest, Long targetItemId) {

    SingleRecommendResponse response = itemService.relateItems(recommendRequest, targetItemId);

    return Response.success(response);
  }

}
