package com.se.apiserver.v1.period.application.service;

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
    Period period = periodJpaRepository.save(Period.builder()
        .periodOrder(101)
        .name("101")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    Long id = period.getPeriodId();

    // When
    PeriodUpdateDto.Request request = PeriodUpdateDto.Request.builder()
        .periodId(id)
        .name("1A")
        .build();

    PeriodReadDto.Response response = periodUpdateService.update(request);

    // Then
    Assertions.assertThat(response.getName()).isEqualTo("1A");
  }

}
