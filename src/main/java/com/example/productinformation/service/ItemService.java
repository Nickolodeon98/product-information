package com.example.productinformation.service;

import com.example.productinformation.domain.dto.DetailedProductInfo;
import com.example.productinformation.domain.dto.ProductInfo;
import com.example.productinformation.domain.dto.request.ProductEditRequest;
import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.ProductDeleteResponse;
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

    return ProductResponse.of(products, "상품 등록 완료");
  }

  public RecommendResponse createRecommend(FileRequest fileRequest) throws IOException {

    List<Recommend> recommends = recommendRepository.saveAll(
        recommendReadLineContext.readLines(fileRequest.getFilename()));

    return RecommendResponse.of(recommends, "상품 등록 완료");
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

    // 입력이 콤마(,)로 시작하거나 끝나거나 숫자와 콤마로 이루어지지 않은 경우 잘못된 입력이다.
    if (itemId.charAt(itemId.length() - 1) == ',' || itemId.charAt(0) == ',' || !itemId.matches(
        "^[0-9,-]*$")) {
      throw new ItemException(ErrorCode.INVALID_INPUT,
          ErrorCode.INVALID_INPUT.getMessage());
    }

    // 만약 콤마(,)가 끼어 있으면 숫자를 하나 하나 모아서 배열에 담은 후에 이들 상품과 연관 상품들을 모두 찾는다.
    if (itemId.contains(",")) {
      String[] ids = itemId.split(",");
      // 이후에 Long 형태의 아이디를 가지고 상품을 찾을 것이므로 모든 아이디를 Long 으로 변환해 Long 배열에 저장해준다.
      Long[] idNums = new Long[ids.length];
      for (int i = 0; i < idNums.length; i++) {
        idNums[i] = Long.valueOf(ids[i]);
      }
      List<Product> products = productRepository.findAllByItemIdIn(idNums)
          .orElseThrow(() -> {
            throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
                ErrorCode.ITEM_NOT_FOUND.getMessage());
          });

      // 싱픔이 있다면 연관 상품을 모두 찾아준다.
      recommends = recommendRepository.findAllByTargetIn(products);

      return ItemResponse.of(ProductInfo.of(products), combineInfo(recommends));
    }

    Product product = productRepository.findByItemId(Long.valueOf(itemId))
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    List<Product> products = new ArrayList<>();

    // 상품 하나이더라도 리스트로 변환해야 이후에 DTO 로 변환할 수 있으므로 리스트에 넣어준다.
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
      // 상품 정보와 연관도 점수, 연관도 순위가 모두 있는 리스트 배열을 만든다.
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

    // 다른 정보가 주어져도 상품 고유 아이디가 주어지지 않으면 잘못된 입력이다.
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

    // 이미 연관 상품을 등록한 적이 있다면 예외 처리한다.
    if (recommend.isPresent()) {
      throw new ItemException(ErrorCode.DUPLICATE_ITEM,
          ErrorCode.DUPLICATE_ITEM.getMessage());
    }
    // 연관을 설정할 대상 상품을 찾는다. 대상 상품이 없는데 연관 관계를 설정하려고 하면 예외 처리한다.
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

    // 연관 상품 정보를 recommend 테이블에 저장한다.
    recommendEntity = recommendRepository.save(recommendRequest.toEntity(product));
    return SingleRecommendResponse.of(recommendEntity, "상품 등록 완료");
  }

  public ProductEditResponse editProduct(String itemId, ProductEditRequest request) {
    itemId = itemId.trim();

    if (itemId.length() == 0 || !itemId.matches("^[0-9]*$")) {
      throw new ItemException(ErrorCode.INVALID_INPUT,
          ErrorCode.INVALID_INPUT.getMessage());
    }

    Product product = productRepository.findByItemId(Long.valueOf(itemId))
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    // 아이디를 이용해 찾은 수정 대상 상품의 정보를 새롭게 저장함으로서 수정한다.
    Product editedProduct = productRepository.save(
        request.toEntity(product.getId(), Long.valueOf(itemId)));

    return ProductEditResponse.of(editedProduct, "상품 수정 완료");
  }

  public ProductDeleteResponse removeProduct(String itemId) {
    itemId = itemId.trim();

    if (itemId.length() == 0 || !itemId.matches("^[0-9]*$")) {
      throw new ItemException(ErrorCode.INVALID_INPUT,
          ErrorCode.INVALID_INPUT.getMessage());
    }

    Product deletedProduct = productRepository.findByItemId(Long.valueOf(itemId))
        .orElseThrow(() -> {
          throw new ItemException(ErrorCode.ITEM_NOT_FOUND,
              ErrorCode.ITEM_NOT_FOUND.getMessage());
        });

    productRepository.deleteById(deletedProduct.getId());

    return ProductDeleteResponse.of(deletedProduct, "상품 삭제 완료");
  }
}
