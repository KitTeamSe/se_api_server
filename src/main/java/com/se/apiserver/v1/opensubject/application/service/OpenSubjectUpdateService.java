package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectUpdateDto;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenSubjectUpdateService {

  private final OpenSubjectJpaRepository openSubjectJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long update(OpenSubjectUpdateDto.Request request){
    OpenSubject openSubject = openSubjectJpaRepository
        .findById(request.getOpenSubjectId())
        .orElseThrow(() -> new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT));

    if(request.getNumberOfDivision() != null){
      openSubject.updateNumberOfDivision(request.getNumberOfDivision());
    }

    if(request.getTeachingTimePerWeek() != null){
      openSubject.updateTeachingTimePerWeek(request.getTeachingTimePerWeek());
    }

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      openSubject.updateNote(note);
    }

    return openSubjectJpaRepository.save(openSubject).getOpenSubjectId();
  }

}
