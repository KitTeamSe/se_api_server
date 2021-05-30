package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectReadDto;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectReadDto.Response;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
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
public class OpenSubjectReadService {

  private final OpenSubjectJpaRepository openSubjectJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;

  public OpenSubjectReadDto.Response read(Long id){
    OpenSubject openSubject = openSubjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT));
    return OpenSubjectReadDto.Response.fromEntity(openSubject);
  }

  public PageImpl readAllByTimeTableId(Pageable pageable, Long timeTableId){

    TimeTable timeTable = timeTableJpaRepository
        .findById(timeTableId)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    Page<OpenSubject> all = openSubjectJpaRepository
        .findAllByTimeTable(pageable, timeTable);
    List<Response> responseList = all
        .stream()
        .map(Response::fromEntity)
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
