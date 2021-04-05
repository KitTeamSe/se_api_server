package com.se.apiserver.v1.period.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.dto.PeriodCreateDto;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PeriodCreateServiceTest {
  
  @Autowired
  PeriodJpaRepository periodJpaRepository;
  
  @Autowired
  PeriodCreateService periodCreateService;
  
  @Test
  void 교시_생성_성공(){
    // Given
    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .note("비고 없음")
        .build();
    
    // When
    Long id = periodCreateService.create(request);

    // Then
    Assertions.assertThat(periodJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 교시_생성_시작시간_종료시간_교차_실패(){
    // Given
    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 50, 0))
        .endTime(LocalTime.of(9, 0, 0))
        .note("비고 없음")
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      periodCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.CROSSING_START_END_TIME.getMessage());
  }

  @Test
  void 교시_생성_교시_순서_중복_실패(){
    // Given
    periodJpaRepository.save(Period.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(1)
        .name("2")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      periodCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.DUPLICATED_PERIOD_ORDER.getMessage());
  }

  @Test
  void 교시_생성_교시_이름_중복_실패(){
    // Given
    periodJpaRepository.save(Period.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(2)
        .name("1")
        .startTime(LocalTime.of(10, 0, 0))
        .endTime(LocalTime.of(10, 50, 0))
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      periodCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.DUPLICATED_PERIOD_NAME.getMessage());
  }
}
