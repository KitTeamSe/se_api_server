package com.se.apiserver.v1.lectureunabletime.domain.entity;

import com.se.apiserver.v1.common.domain.error.GlobalErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;

public enum DayOfWeek {
  MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

  public static DayOfWeek fromShortKorean(String str){
    switch(str){
      case "월":
        return MONDAY;
      case "화":
        return TUESDAY;
      case "수":
        return WEDNESDAY;
      case "목":
        return THURSDAY;
      case "금":
        return FRIDAY;
      case "토":
        return SATURDAY;
      case "일":
        return SUNDAY;
      default:
        throw new BusinessException(GlobalErrorCode.INVALID_ENUM_INPUT);
    }
  }
}
