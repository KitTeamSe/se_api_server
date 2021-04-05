package com.se.apiserver.v1.period.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PeriodDeleteUseCaseTest {

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Autowired
  PeriodDeleteUseCase periodDeleteUseCase;

  @Test
  void 교시_삭제_성공(){
    // Given
    Period period = periodJpaRepository.save(Period.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    Long id = period.getPeriodId();

    // When
    periodDeleteUseCase.delete(id);

    // Then
    Assertions.assertThat(periodJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 교시_미존재_삭제_실패(){
    // Given
    Long id = 999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      periodDeleteUseCase.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.NO_SUCH_PERIOD.getMessage());
  }
}
