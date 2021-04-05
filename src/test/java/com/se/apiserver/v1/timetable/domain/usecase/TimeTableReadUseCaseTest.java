package com.se.apiserver.v1.timetable.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.domain.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableReadDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TimeTableReadUseCaseTest {

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  TimeTableReadUseCase timeTableReadUseCase;

  @Test
  void 시간표_조회_성공(){
    // Given
    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    // When
    TimeTableReadDto.Response response = timeTableReadUseCase.read(timeTable.getTimeTableId());

    // Then
    Assertions.assertThat(timeTable.getName()).isEqualTo(response.getName());
    Assertions.assertThat(timeTable.getYear()).isEqualTo(response.getYear());
    Assertions.assertThat(timeTable.getSemester()).isEqualTo(response.getSemester());
    Assertions.assertThat(timeTable.getStatus()).isEqualTo(response.getStatus());
    Assertions.assertThat(timeTable.getCreatedAccountBy()).isEqualTo(response.getCreatedBy());
  }

  @Test
  void 시간표_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      timeTableReadUseCase.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

  @Test
  void 시간표_전체_조회_성공(){
    // Given
    timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 10")
        .year(2021)
        .semester(1)
        .status(TimeTableStatus.CREATED)
        .build());

    timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 11")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    // When
    PageImpl responses = timeTableReadUseCase.readAll(PageRequest.builder()
        .size(100)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
