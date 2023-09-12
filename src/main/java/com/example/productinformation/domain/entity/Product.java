package com.example.productinformation.domain.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  // 아이디가 3억 대로 되어 있으므로 여유 있게 Long 으로 설정한다.
  private Long itemId;
  private String itemName;
  private String itemImage;
  private String itemUrl;
  // 상품의 가격이 22억을 넘는 경우는 없으므로 int 로 설정한다.
  private Integer originalPrice;
  private Integer salePrice;
}
