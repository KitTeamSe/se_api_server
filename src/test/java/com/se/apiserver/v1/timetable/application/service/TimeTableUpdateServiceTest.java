package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.dto.TimeTableUpdateDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TimeTableUpdateServiceTest {
  
  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  TimeTableUpdateService timeTableUpdateService;

  @Test
  void 시간표_수정_이름_수정_성공(){
    // Given
    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    Long id = timeTable.getTimeTableId();

    // When
    String alterName = "테스트 시간표 1 - 수정됨";
    TimeTableUpdateDto.Request request = TimeTableUpdateDto.Request.builder()
        .timeTableId(id)
        .name(alterName)
        .build();

    Long updated_id = timeTableUpdateService.update(request);
    TimeTable updated = timeTableJpaRepository
        .findById(updated_id)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    // Then
    Assertions.assertThat(updated.getName()).isEqualTo(alterName);
    Assertions.assertThat(updated.getYear()).isEqualTo(timeTable.getYear());
    Assertions.assertThat(updated.getSemester()).isEqualTo(timeTable.getSemester());
    Assertions.assertThat(updated.getStatus()).isEqualTo(timeTable.getStatus());
  }

  @Test
  void 시간표_수정_모두_수정_성공(){
    // Given
    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 2")
        .year(2022)
        .semester(1)
        .status(TimeTableStatus.CREATED)
        .build());

    Long id = timeTable.getTimeTableId();

    // When
    String alterName = "테스트 시간표 2 - 수정됨";
    Integer alterYear = 2023;
    Integer alterSemester = 2;
    TimeTableStatus alterStatus = TimeTableStatus.INSERT_LIBERAL_ARTS;

    TimeTableUpdateDto.Request request = TimeTableUpdateDto.Request.builder()
        .timeTableId(id)
        .name(alterName)
        .year(alterYear)
        .semester(alterSemester)
        .status(alterStatus)
        .build();

    Long updated_id = timeTableUpdateService.update(request);
    TimeTable updated = timeTableJpaRepository
        .findById(updated_id)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    // Then
    Assertions.assertThat(updated.getName()).isEqualTo(alterName);
    Assertions.assertThat(updated.getYear()).isEqualTo(alterYear);
    Assertions.assertThat(updated.getSemester()).isEqualTo(alterSemester);
    Assertions.assertThat(updated.getStatus()).isEqualTo(alterStatus);
  }

  @Test
  void 시간표_수정_존재하지_않는_시간표_실패(){
    // Given
    Long id = 777L;

    // When
    String alterName = "테스트 시간표 2 - 수정됨";
    Integer alterYear = 2023;
    Integer alterSemester = 2;
    TimeTableStatus alterStatus = TimeTableStatus.INSERT_LIBERAL_ARTS;

    TimeTableUpdateDto.Request request = TimeTableUpdateDto.Request.builder()
        .timeTableId(id)
        .name(alterName)
        .year(alterYear)
        .semester(alterSemester)
        .status(alterStatus)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      timeTableUpdateService.update(request);
    }).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

  @Test
  void 시간표_수정_시간표_이름_중복_실패(){
    // Given
    timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(1)
        .status(TimeTableStatus.CREATED)
        .build());

    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 2")
        .year(2022)
        .semester(1)
        .status(TimeTableStatus.CREATED)
        .build());

    Long id = timeTable.getTimeTableId();

    // When
    String alterName = "테스트 시간표 1";

    TimeTableUpdateDto.Request request = TimeTableUpdateDto.Request.builder()
        .timeTableId(id)
        .name(alterName)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      timeTableUpdateService.update(request);
    }).hasMessage(TimeTableErrorCode.DUPLICATED_TIME_TABLE_NAME.getMessage());
  }
}
