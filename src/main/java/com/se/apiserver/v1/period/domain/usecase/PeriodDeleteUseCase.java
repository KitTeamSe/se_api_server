package com.se.apiserver.v1.period.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PeriodDeleteUseCase {

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
