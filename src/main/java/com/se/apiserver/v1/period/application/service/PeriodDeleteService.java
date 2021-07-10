package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeriodDeleteService {

  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public void delete(Long periodId){
    Period period = periodJpaRepository
        .findById(periodId)
        .orElseThrow(()-> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD)
        );

    periodJpaRepository.delete(period);
  }
}
