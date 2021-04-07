package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.dto.PeriodCreateDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeriodCreateService {

  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public Long create(PeriodCreateDto.Request request){

    // 교시 순서가 유니크한지 검사
    checkUniquePeriodOrder(request.getPeriodOrder());

    // 교시 이름이 유니크한지 검사
    checkUniqueName(request.getName());

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
}
