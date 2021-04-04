package com.se.apiserver.v1.timetable.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.domain.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.dto.TimeTableCreateDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeTableCreateUseCase {

  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long create(TimeTableCreateDto.Request request){
    // 건물명과 호수가 겹치는 강의실이 있는지 검사
    if(timeTableJpaRepository.findByName(request.getName()).isPresent()){
      throw new BusinessException(TimeTableErrorCode.DUPLICATED_TIME_TABLE_NAME);
    }

    TimeTable timeTable = TimeTable.builder()
        .name(request.getName())
        .year(request.getYear())
        .semester(request.getSemester())
        .status(TimeTableStatus.CREATED)
        .build();

    timeTableJpaRepository.save(timeTable);

    return timeTable.getTimeTableId();
  }
}
