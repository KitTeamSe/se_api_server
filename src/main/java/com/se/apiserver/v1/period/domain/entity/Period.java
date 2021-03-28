package com.se.apiserver.v1.period.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Period {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long periodId;

  @Column(nullable = false, unique = true)
  private Integer periodOrder;

  @Size(min = 2, max = 20)
  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @Size(min = 2, max = 255)
  private String note;

  @Builder
  public Period(Long periodId, Integer periodOrder,
      @Size(min = 2, max = 20) String name, LocalDateTime startTime, LocalDateTime endTime,
      @Size(min = 2, max = 255) String note) {
    this.periodId = periodId;
    this.periodOrder = periodOrder;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.note = note;
  }
}
