package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.dto.TimeTableUpdateDto;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimeTableUpdateService {

  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long update(TimeTableUpdateDto.Request request) {

    TimeTable timeTable = timeTableJpaRepository
        .findById(request.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    if(request.getName() != null){
      if(timeTableJpaRepository.findByName(request.getName()).isPresent())
        throw new BusinessException(TimeTableErrorCode.DUPLICATED_TIME_TABLE_NAME);
      updateName(timeTable, request.getName());
    }

    if(request.getYear() != null)
      updateYear(timeTable, request.getYear());

    if(request.getSemester() != null)
      updateSemester(timeTable, request.getSemester());

    if(request.getStatus() != null)
      updateStatus(timeTable, request.getStatus());

    // 저장
    TimeTable t = timeTableJpaRepository.save(timeTable);
    return t.getTimeTableId();
  }

  public void updateName(TimeTable timeTable, String name){
    timeTable.updateName(name);
  }

  public void updateYear(TimeTable timeTable, Integer year){
    timeTable.updateYear(year);
  }

  public void updateSemester(TimeTable timeTable, Integer semester){
    timeTable.updateSemester(semester);
  }

  public void updateStatus(TimeTable timeTable, TimeTableStatus status){
    timeTable.updateStatus(status);
  }

}
