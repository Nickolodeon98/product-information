package com.example.productinformation.service;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import com.example.productinformation.domain.dto.request.ProductEditRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.ProductEditResponse;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.dto.response.SingleRecommendResponse;
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
import java.util.Optional;
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
   *
   * @param itemId
   * @return ItemResponse
   */
  public ItemResponse acquireItem(String itemId) {
    List<Recommend> recommends = new ArrayList<>();

    // 입력에 공백이 들어갈 경우를 감안해서 공백을 먼저 제거해준다.
    itemId = itemId.trim();

    if (itemId.charAt(itemId.length() - 1) == ',' || itemId.charAt(0) == ',') {
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
          .orElseThrow(() -> {
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
                ErrorCode.ITEM_NOT_FOUND.getMessage());
          });

      recommends = recommendRepository.findAllByTargetIn(products);

      return ItemResponse.of(ProductInfo.of(products), combineInfo(recommends));
    }

    Product product = productRepository.findByItemId(Long.valueOf(itemId))
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    List<Product> products = new ArrayList<>();
    products.add(product);

    recommends = recommendRepository.findAllByTarget(product);

    return ItemResponse.of(ProductInfo.of(products), combineInfo(recommends));
  }

  /**
   * 연관 상품 목록을 받으면 상품의 상세 정보를 연관도 정보와 함께 병합한다.
   *
   * @param recommends
   * @return List
   */
  private List<DetailedProductInfo> combineInfo(List<Recommend> recommends) {
    List<DetailedProductInfo> detailedProductInfos = new ArrayList<>();

    for (Recommend recommend : recommends) {
      Product productInfo = productRepository.findByItemId(recommend.getItemId())
          .orElseThrow(() -> {
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
                ErrorCode.ITEM_NOT_FOUND.getMessage());
          });

      detailedProductInfos.add(DetailedProductInfo.of(productInfo, recommend));
    }

    return detailedProductInfos;
  }

  /**
   * 입력으로 주어진 상품 정보를 DB에 저장한다.
   *
   * @param productRequest
   * @return
   */
  public ProductInfo extraProduct(ProductInfo productRequest) {
    if (productRequest.getItemId() == null) {
      throw new ItemException(ErrorCode.INVALID_INPUT,
          ErrorCode.INVALID_INPUT.getMessage());
    }

    Optional<Product> product = productRepository.findByItemId(productRequest.getItemId());
    if (product.isPresent()) {
      throw new ItemException(ErrorCode.DUPLICATE_ITEM,
          ErrorCode.DUPLICATE_ITEM.getMessage());
    }

    Product savedProduct = productRepository.save(productRequest.toEntity());

    return ProductInfo.of(savedProduct);
  }

  public SingleRecommendResponse relateItems(DetailedProductInfo recommendRequest,
      Long targetItemId) {

    Optional<Recommend> recommend = recommendRepository.findByItemId(recommendRequest.getItemId());

    // 이미 연관 상품을 등록한 적이 있다면 예외 처리
    if (recommend.isPresent()) {
      throw new ItemException(ErrorCode.DUPLICATE_ITEM,
          ErrorCode.DUPLICATE_ITEM.getMessage());
    }
    // 연관을 설정할 대상 상품을 찾는다. 대상 상품이 없는데 연관 관계를 설정하려고 하면 예외 처리
    Product product = productRepository.findByItemId(targetItemId)
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    // 현재 연관 상품으로 등록되는 상품이 이미 존재하는지 확인한다.
    Optional<Product> recommendedItem = productRepository.findByItemId(
        recommendRequest.getItemId());
    Recommend recommendEntity = null;

    // 존재하지 않는다면 product 테이블에도 저장한다.
    if (recommendedItem.isEmpty()) {
      productRepository.save(recommendRequest.toProductEntity());
    }

    recommendEntity = recommendRepository.save(recommendRequest.toEntity(product));
    return SingleRecommendResponse.of(recommendEntity, "등록 완료");
  }

  public ProductEditResponse editProduct(Long itemId, ProductEditRequest request) {

    productRepository.findByItemId(itemId)
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    Product editedProduct = productRepository.save(request.toEntity(itemId));

    return ProductEditResponse.of(editedProduct, "수정 완료");
  }
}
