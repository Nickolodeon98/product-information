package com.example.productinformation.controller;

import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.Response;
import com.example.productinformation.domain.dto.response.RecommendResponse;
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
  @Operation(summary = "상품 등록", description = "입력된 파일 이름으로부터 상품 데이터를 추출하여 DB에 저장할 수 있다.")
  @PostMapping("/items/new")
  public Response<ProductResponse> registerItems(@RequestBody FileRequest fileRequest)
      throws IOException {
    ProductResponse productResponse = itemService.createProduct(fileRequest);

    return Response.success(productResponse);
  }

  @ResponseBody
  @Operation(summary = "연관 상품 등록", description = "입력된 파일 이름으로부터 연관 상품 데이터를 추출하여 DB에 저장할 수 있다.")
  @PostMapping("/items/relevance")
  public Response<RecommendResponse> registerRecommends(@RequestBody FileRequest fileRequest)
      throws IOException {

    RecommendResponse recommendResponse = itemService.createRecommend(fileRequest);

    return Response.success(recommendResponse);
  }

  @ResponseBody
  @Operation(summary = "상품 조회", description = "상품 정보 및 연관 상품에 대한 조회")
  @GetMapping
  public Response<ItemResponse> searchItems(@RequestParam("id") String itemId) {

    // itemId 를 Long 으로 변환해준다.
    ItemResponse itemResponse = itemService.acquireItem(itemId);

    return Response.success(itemResponse);
  }
}
