package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PeriodDeleteServiceTest {

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Autowired
  PeriodDeleteService periodDeleteService;

  @Test
  void 교시_삭제_성공(){
    // Given
    Period period = periodJpaRepository.save(Period.builder()
        .periodOrder(101)
        .name("101")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    Long id = period.getPeriodId();

    // When
    periodDeleteService.delete(id);

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
      periodDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.NO_SUCH_PERIOD.getMessage());
  }
}
