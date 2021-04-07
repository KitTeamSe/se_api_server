package com.se.apiserver.v1.period.domain.entity;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.application.error.PeriodRangeError;
import com.se.apiserver.v1.period.domain.entity.Period;
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

    validatePeriod();
  }

  public void validatePeriod(){
    if(startPeriod.getPeriodOrder() > endPeriod.getPeriodOrder())
      throw new BusinessException(PeriodRangeError.INVALID_PERIOD_RANGE);
  }
}
