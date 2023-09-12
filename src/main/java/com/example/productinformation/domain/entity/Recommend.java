package com.example.productinformation.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Recommend {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long itemId;

  @ManyToOne
  private Product product;

  private Integer score;
  private Integer rank;
}
