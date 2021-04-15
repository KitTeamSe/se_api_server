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
        .periodOrder(101)
        .name("101")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
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
        .periodOrder(101)
        .name("101")
        .startTime(LocalTime.of(9, 50, 0))
        .endTime(LocalTime.of(9, 0, 0))
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
    createPeriod(periodJpaRepository, 101, "101", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0));

    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(101)
        .name("102")
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
    createPeriod(periodJpaRepository, 101, "101", LocalTime.of(9, 0, 0), LocalTime.of(9, 50, 0));

    PeriodCreateDto.Request request = PeriodCreateDto.Request.builder()
        .periodOrder(102)
        .name("101")
        .startTime(LocalTime.of(10, 0, 0))
        .endTime(LocalTime.of(10, 50, 0))
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      periodCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.DUPLICATED_PERIOD_NAME.getMessage());
  }

  public static Period createPeriod(PeriodJpaRepository periodJpaRepository, Integer periodOrder, String name, LocalTime startTime, LocalTime endTime){
    return periodJpaRepository.save(new Period(periodOrder, name, startTime, endTime));
  }

  public static Period getPeriod(PeriodJpaRepository periodJpaRepository, String name){
    return periodJpaRepository
        .findByName(name)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
  }
}
