package com.example.productinformation.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Slf4j
public class BaseEntity {

  @CreatedDate
  @Column(updatable=false)
  private LocalDateTime createdAt;

  private LocalDateTime deletedAt;

  @PrePersist
  public void beforeCreation() {
    LocalDateTime localDateTime = LocalDateTime
        .of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
            LocalTime.now().getHour(), LocalTime.now().getMinute(), LocalTime.now().getSecond());

    this.createdAt = localDateTime;
  }
}
