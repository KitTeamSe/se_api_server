package com.se.apiserver.v1.period.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period extends AccountGenerateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long periodId;

  @Column(nullable = false, unique = true)
  private Integer periodOrder;

  @Size(min = 1, max = 20)
  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  @Size(min = 1, max = 255)
  private String note;

  @Builder
  public Period(Long periodId, Integer periodOrder,
      @Size(min = 1, max = 20) String name, LocalTime startTime, LocalTime endTime,
      @Size(min = 2, max = 255) String note) {
    this.periodId = periodId;
    this.periodOrder = periodOrder;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.note = note;
  }
}
