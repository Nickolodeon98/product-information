package com.example.productinformation.service;

import com.example.productinformation.domain.dto.DetailedInfo;
import com.example.productinformation.domain.dto.TargetInfo;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.exception.ErrorCode;
import com.example.productinformation.exception.ItemException;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import com.example.productinformation.repository.RecommendRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

  private final ReadLineContext<Product> productReadLineContext;

  private final ReadLineContext<Recommend> recommendReadLineContext;
  private final ProductRepository productRepository;

  private final RecommendRepository recommendRepository;


  public ProductResponse createProduct(FileRequest fileRequest) throws IOException {

    List<Product> products = productRepository.saveAll(
        productReadLineContext.readLines(fileRequest.getFilename()));

    return ProductResponse.of(products, "등록 완료");
  }

  public RecommendResponse createRecommend(FileRequest fileRequest) throws IOException {

    List<Recommend> recommends = recommendRepository.saveAll(
        recommendReadLineContext.readLines(fileRequest.getFilename()));

    return RecommendResponse.of(recommends, "등록 완료");
  }

  /**
   * 파라미터로 주어진 상품 아이디에 따라 상품 정보와 연관 상품 정보를 찾아 반환한다.
   * @param itemId
   * @return ItemResponse
   */
  public ItemResponse acquireItem(String itemId) {
    List<Recommend> recommends = new ArrayList<>();

    // 입력에 공백이 들어갈 경우를 감안해서 공백을 먼저 제거해준다.
    itemId = itemId.trim();

    if (itemId.charAt(itemId.length()-1) == ',' || itemId.charAt(0) == ',') {
      throw new ItemException(ErrorCode.INVALID_INPUT,
          ErrorCode.INVALID_INPUT.getMessage());
    }

    if (itemId.contains(",")) {
      String[] ids = itemId.split(",");
      Long[] idNums = new Long[ids.length];
      for (int i = 0; i < idNums.length; i++) {
        idNums[i] = Long.valueOf(ids[i]);
      }
      List<Product> products = productRepository.findAllByItemIdIn(idNums)
          .orElseThrow(()->{
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
                ErrorCode.ITEM_NOT_FOUND.getMessage());
          });

      recommends = recommendRepository.findAllByTargetIn(products);

      return ItemResponse.of(TargetInfo.of(products), combineInfo(recommends));
    }

    Product product = productRepository.findByItemId(Long.valueOf(itemId))
        .orElseThrow(()->{
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    List<Product> products = new ArrayList<>();
    products.add(product);

    recommends = recommendRepository.findAllByTarget(product);

    return ItemResponse.of(TargetInfo.of(products), combineInfo(recommends));
  }

  /**
   * 연관 상품 목록을 받으면 상품의 상세 정보를 연관도 정보와 함께 병합한다.
   * @param recommends
   * @return List
   */
  private List<DetailedInfo> combineInfo(List<Recommend> recommends) {
    List<DetailedInfo> detailedInfos = new ArrayList<>();

    for (Recommend recommend : recommends) {
      Product productInfo = productRepository.findByItemId(recommend.getItemId())
          .orElseThrow(() -> {
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
                ErrorCode.ITEM_NOT_FOUND.getMessage());
          });

      detailedInfos.add(DetailedInfo.of(productInfo, recommend));
    }

    return detailedInfos;
  }
}
