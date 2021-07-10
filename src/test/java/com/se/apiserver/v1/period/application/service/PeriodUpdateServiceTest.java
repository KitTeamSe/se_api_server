package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto;
import com.se.apiserver.v1.period.application.dto.PeriodUpdateDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PeriodUpdateServiceTest {

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Autowired
  PeriodUpdateService periodUpdateService;

  @Test
  void 교시_수정_성공(){
    // Given
    Period period = PeriodCreateServiceTest
        .createPeriod(periodJpaRepository, 101, "101", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0));

    Long id = period.getPeriodId();

    // When
    PeriodUpdateDto.Request request = PeriodUpdateDto.Request.builder()
        .periodId(id)
        .name("1A")
        .build();

    Long receivedId = periodUpdateService.update(request);

    Period receivedPeriod = periodJpaRepository.findById(receivedId)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));

    // Then
    Assertions.assertThat(receivedPeriod.getName()).isEqualTo("1A");
  }

}
