package com.se.apiserver.v1.period.domain.entity;

import com.se.apiserver.v1.common.domain.entity.AccountGenerateEntity;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
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

  @Size(max = 255)
  private String note;

  @Builder
  public Period(Long periodId, Integer periodOrder,
      @Size(min = 1, max = 20) String name, LocalTime startTime, LocalTime endTime,
      @Size(max = 255) String note) {

    validatePeriodOrder(periodOrder);

    this.periodId = periodId;
    this.periodOrder = periodOrder;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.note = note;

    validateTimeCrossing();
  }

  public void validateTimeCrossing(){
    if(startTime.isAfter(endTime))
      throw new BusinessException(PeriodErrorCode.CROSSING_START_END_TIME);
  }

  public void validatePeriodOrder(Integer periodOrder){
    if(periodOrder < 0)
      throw new BusinessException(PeriodErrorCode.INVALID_PERIOD_ORDER);
  }

  public void updatePeriodOrder(Integer periodOrder){
    validatePeriodOrder(periodOrder);
    this.periodOrder = periodOrder;
  }

  public void updateName(String name){
    this.name = name;
  }

  public void updateStartTime(LocalTime startTime){
    this.startTime = startTime;
  }

  public void updateEndTime(LocalTime endTime){
    this.endTime = endTime;
  }

  public void updateNote(String note){
    this.note = note;
  }
}
