package com.se.apiserver.v1.timetable.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.application.dto.TimeTableReadDto;
import com.se.apiserver.v1.timetable.application.dto.TimeTableReadDto.Response;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TimeTableReadService {

  private final TimeTableJpaRepository timeTableJpaRepository;

  public TimeTableReadDto.Response read(Long id){
    TimeTable timeTable = timeTableJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));
    return TimeTableReadDto.Response.fromEntity(timeTable);
  }

  public PageImpl readAll(Pageable pageable){
    Page<TimeTable> all = timeTableJpaRepository.findAll(pageable);
    List<Response> responseList = all
        .stream()
        .map(lr -> TimeTableReadDto.Response.fromEntity(lr))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }

}
