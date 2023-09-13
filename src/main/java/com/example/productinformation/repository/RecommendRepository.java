package com.example.productinformation.repository;

import com.example.productinformation.domain.entity.Product;
import com.example.productinformation.domain.entity.Recommend;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long> {

  List<Recommend> findAllByTarget(Product product);

  List<Recommend> findAllByTargetIn(List<Product> products);

  Optional<Recommend> findByItemId(Long itemId);
}
