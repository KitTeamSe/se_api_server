package com.se.apiserver.v1.period.domain.entity;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.application.error.PeriodRangeError;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeriodRange {

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "start_period_id", referencedColumnName = "periodId", nullable = false)
  private Period startPeriod;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
  @JoinColumn(name = "end_period_id", referencedColumnName = "periodId", nullable = false)
  private Period endPeriod;

  public PeriodRange(Period startPeriod, Period endPeriod){
    this.startPeriod = startPeriod;
    this.endPeriod = endPeriod;

    validateCrossing();
  }

  public void validateCrossing(){
    if(startPeriod.getPeriodOrder() > endPeriod.getPeriodOrder())
      throw new BusinessException(PeriodRangeError.INVALID_PERIOD_RANGE);
  }

  public boolean isOverlappedWith(PeriodRange target){
    PeriodRange first = this;
    PeriodRange second = target;
    if(startPeriod.isAfter(target.startPeriod)){
      first = target;
      second = this;
    }

    if(first.getEndPeriod().equals(second.startPeriod))
      return true;

    return first.getEndPeriod().isAfter(second.getStartPeriod());
  }

  // 수업 시간
  public int getTeachingTime(){
    return endPeriod.getPeriodOrder() - startPeriod.getPeriodOrder() + 1;
  }
}
