package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.dto.TimeTableCreateDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TimeTableCreateServiceTest {

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  TimeTableCreateService timeTableCreateService;

  @Test
  void 시간표_추가_성공(){
    // Given
    TimeTableCreateDto.Request request = TimeTableCreateDto.Request.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .build();

    // When
    Long id = timeTableCreateService.create(request);

    // Then
    Assertions.assertThat(timeTableJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 시간표_추가_이름_중복_실패(){
    // Given
    timeTableJpaRepository.save(TimeTable.builder()
        .name("중복 테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    // When
    TimeTableCreateDto.Request request = TimeTableCreateDto.Request.builder()
        .name("중복 테스트 시간표 1")
        .year(2022)
        .semester(1)
        .build();

    // Then
    Assertions.assertThatThrownBy(()-> timeTableCreateService.create(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(TimeTableErrorCode.DUPLICATED_TIME_TABLE_NAME.getMessage());
  }
}
