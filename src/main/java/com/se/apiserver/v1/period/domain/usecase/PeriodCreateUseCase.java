package com.se.apiserver.v1.period.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.dto.PeriodCreateDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeriodCreateUseCase {

  @Autowired
  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public Long create(PeriodCreateDto.Request request){

    // 교시 순서가 유니크한지 검사
    checkUniquePeriodOrder(request.getPeriodOrder());

    // 교시 이름이 유니크한지 검사
    checkUniqueName(request.getName());

    // LocalDateTime의 날짜정보를 0년 1월 1일로 바꾸어 시간만 남김.

    // 시작 시간과 종료 시간이 교차하는지 검사
    checkTimeCrossing(request.getStartTime(), request.getEndTime());

    Period period = Period.builder()
        .periodOrder(request.getPeriodOrder())
        .name(request.getName())
        .startTime(request.getStartTime())
        .endTime(request.getEndTime())
        .note(request.getNote())
        .build();

    periodJpaRepository.save(period);

    return period.getPeriodId();
  }

  private void checkUniquePeriodOrder(int periodOrder){
    if(periodJpaRepository.findByPeriodOrder(periodOrder).isPresent())
      throw new BusinessException(PeriodErrorCode.DUPLICATED_PERIOD_ORDER);
  }

  private void checkUniqueName(String name){
    if(periodJpaRepository.findByName(name).isPresent())
      throw new BusinessException(PeriodErrorCode.DUPLICATED_PERIOD_NAME);
  }

  private void checkTimeCrossing(LocalTime startTime, LocalTime endTime){
    if(startTime.isAfter(endTime))
      throw new BusinessException(PeriodErrorCode.CROSSING_START_END_TIME);
  }
}
