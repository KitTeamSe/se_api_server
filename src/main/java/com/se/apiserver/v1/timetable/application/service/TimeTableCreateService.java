package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.dto.TimeTableCreateDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeTableCreateService {

  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long create(TimeTableCreateDto.Request request){
    // 이름이 중복되는 시간표가 있는지 검사
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
