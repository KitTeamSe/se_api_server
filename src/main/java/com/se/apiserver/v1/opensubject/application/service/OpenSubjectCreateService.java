package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectCreateDto;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenSubjectCreateService {

  private final OpenSubjectJpaRepository openSubjectJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;
  private final SubjectJpaRepository subjectJpaRepository;

  @Transactional
  public Long create(OpenSubjectCreateDto.Request request){

    TimeTable timeTable = timeTableJpaRepository.findById(request.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    Subject subject = subjectJpaRepository.findById(request.getSubjectId())
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));

    if(openSubjectJpaRepository.findByTimeTableAndSubject(timeTable, subject).isPresent())
      throw new BusinessException(OpenSubjectErrorCode.DUPLICATED_OPEN_SUBJECT);

    // 주간 강의 시간이 정해져있지 않으면 교과의 학점을 그대로 사용.
    if(request.getTeachingTimePerWeek() == null)
      request.setTeachingTimePerWeek(subject.getCredit());

    OpenSubject openSubject = new OpenSubject(
        timeTable,
        subject,
        request.getTeachingTimePerWeek(),
        false,
        request.getNote());

    return openSubjectJpaRepository.save(openSubject).getOpenSubjectId();
  }

}
