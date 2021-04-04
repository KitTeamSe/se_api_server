package com.se.apiserver.v1.timetable.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.domain.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TimeTableDeleteUseCaseTest {

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  TimeTableDeleteUseCase timeTableDeleteUseCase;

  @Test
  void 시간표_삭제_성공(){
    // Given
    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    Long id = timeTable.getTimeTableId();

    // When
    timeTableDeleteUseCase.delete(id);

    // Then
    Assertions.assertThat(timeTableJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 시간표_미존재_삭제_실패(){
    // Given
    Long id = 999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      timeTableDeleteUseCase.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }
}
