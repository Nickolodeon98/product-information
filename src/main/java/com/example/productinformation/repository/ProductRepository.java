package com.example.productinformation.repository;

import com.example.productinformation.domain.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
  Optional<Product> findByItemId(Long itemId);
}
