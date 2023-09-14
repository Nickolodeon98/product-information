package com.example.productinformation.domain.entity;


import com.example.productinformation.domain.dto.ProductInfo;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Delete 시 실제로 삭제되지 않고 deleted_at 컬럼이 삭제 시도 시간으로 채워진다.
@SQLDelete(sql = "UPDATE product SET deleted_at = current_timestamp WHERE id = ?")
// 삭제된 적이 없는 엔티티만 조회된다.
@Where(clause = "deleted_at IS NULL")
public class Product extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  // 아이디가 3억 대로 되어 있으므로 여유 있게 Long 으로 설정한다.
  private Long itemId;
  private String itemName;
  private String itemImage;
  private String itemUrl;
  // 상품의 가격이 22억을 넘는 경우는 없으므로 int 로 설정한다.
  private Integer originalPrice;
  private Integer salePrice;

  public ProductInfo toRequest() {
    return ProductInfo.builder()
        .itemId(this.itemId)
        .itemName(this.itemName)
        .itemImage(this.itemImage)
        .itemUrl(this.itemUrl)
        .originalPrice(this.originalPrice)
        .salePrice(this.salePrice)
        .build();
  }
}
