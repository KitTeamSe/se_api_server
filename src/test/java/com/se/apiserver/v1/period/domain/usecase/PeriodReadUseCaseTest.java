package com.se.apiserver.v1.period.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.error.PeriodErrorCode;
import com.se.apiserver.v1.period.infra.dto.PeriodReadDto;
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
public class PeriodReadUseCaseTest {

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Autowired
  PeriodReadUseCase periodReadUseCase;

  @Test
  void 교시_조회_성공(){
    // Given
    Period period = periodJpaRepository.save(Period.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    // When
    PeriodReadDto.Response response = periodReadUseCase.read(period.getPeriodId());

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
      periodReadUseCase.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(PeriodErrorCode.NO_SUCH_PERIOD.getMessage());
  }

  @Test
  void 강의실_전체_조회_성공(){
    // Given
    periodJpaRepository.save(Period.builder()
        .periodOrder(1)
        .name("1")
        .startTime(LocalTime.of(9, 0, 0))
        .endTime(LocalTime.of(9, 50, 0))
        .build());

    periodJpaRepository.save(Period.builder()
        .periodOrder(2)
        .name("2")
        .startTime(LocalTime.of(10, 0, 0))
        .endTime(LocalTime.of(10, 50, 0))
        .build());

    // When
    PageImpl responses = periodReadUseCase.readAll(PageRequest.builder()
        .size(100)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
