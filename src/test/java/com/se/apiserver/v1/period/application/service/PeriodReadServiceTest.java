package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.dto.PeriodReadDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PeriodReadServiceTest {

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Autowired
  PeriodReadService periodReadService;

  @Test
  void 교시_조회_성공(){
    // Given
    Period period = PeriodCreateServiceTest
        .createPeriod(periodJpaRepository, 101, "101", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0));

    // When
    PeriodReadDto.Response response = periodReadService.read(period.getPeriodId());

    // Then
    Assertions.assertThat(period.getPeriodOrder()).isEqualTo(response.getPeriodOrder());
    Assertions.assertThat(period.getName()).isEqualTo(response.getName());
    Assertions.assertThat(period.getStartTime()).isEqualTo(response.getStartTime());
    Assertions.assertThat(period.getEndTime()).isEqualTo(response.getEndTime());
  }

  @Test
  void 교시_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      periodReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.NO_SUCH_PERIOD.getMessage());
  }

  @Test
  void 교시_전체_조회_성공(){
    // Given
    PeriodCreateServiceTest
        .createPeriod(periodJpaRepository, 101, "101", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0));

    PeriodCreateServiceTest
        .createPeriod(periodJpaRepository, 102, "102", LocalTime.of(10, 0, 0), LocalTime.of(10, 50, 0));

    // When
    PageImpl responses = periodReadService.readAll(PageRequest.builder()
        .size(100)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(16); // 2개가 되어야 하지만, init_sql로 14개가 생성되므로 16이 맞음.
  }
}
