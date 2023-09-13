package com.example.productinformation.service;

import com.example.productinformation.domain.dto.response.ItemResponse;
import com.example.productinformation.domain.dto.response.RecommendResponse;
import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.dto.request.FileRequest;
import com.example.productinformation.domain.dto.response.ProductResponse;
import com.example.productinformation.domain.entity.Recommend;
import com.example.productinformation.parser.ReadLineContext;
import com.example.productinformation.repository.ProductRepository;
import com.example.productinformation.repository.RecommendRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ReadLineContext<Product> productReadLineContext;

  private final ReadLineContext<Recommend> recommendReadLineContext;
  private final ProductRepository productRepository;

  private final RecommendRepository recommendRepository;


  public ProductResponse createProduct(FileRequest fileRequest) throws IOException {

    List<Product> products = productRepository.saveAll(productReadLineContext.readLines(fileRequest.getFilename()));

    return ProductResponse.of(products, "등록 완료");
  }

  public RecommendResponse createRecommend(FileRequest fileRequest) throws IOException {

    List<Recommend> recommends = recommendRepository.saveAll(recommendReadLineContext.readLines(fileRequest.getFilename()));

    return RecommendResponse.of(recommends, "등록 완료");
  }

  public ItemResponse acquireItem(String itemId) {
    List<Product> products = new ArrayList<>();
    List<Recommend> recommends = new ArrayList<>();

    if (itemId.contains(",")) {
      String[] ids = itemId.split(",");
      for (String id : ids) {
        Product product = productRepository.findByItemId(Long.valueOf(id)).get();
        products.add(product);
        recommends = recommendRepository.findAllByTarget(product);
      }
      return ItemResponse.of(products, recommends);
    }

    Product product = productRepository.findByItemId(Long.valueOf(itemId)).get();

    products.add(product);
    recommends = recommendRepository.findAllByTarget(product);

    return ItemResponse.of(products, recommends);
  }
}
