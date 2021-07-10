package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeTableDeleteService {

  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public void delete(Long timeTableId){
    TimeTable timeTable = timeTableJpaRepository
        .findById(timeTableId)
        .orElseThrow(()-> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE)
        );

    timeTableJpaRepository.delete(timeTable);
  }
}
