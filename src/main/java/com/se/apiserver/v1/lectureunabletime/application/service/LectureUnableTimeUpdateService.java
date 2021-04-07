package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeUpdateDto;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureUnableTimeUpdateService {

  private final LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public Long update(LectureUnableTimeUpdateDto.Request request){
    LectureUnableTime lectureUnableTime = lectureUnableTimeJpaRepository
        .findById(request.getLectureUnableTimeId())
        .orElseThrow(() -> new BusinessException(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME));

    if(request.getDayOfWeek() != null){
      lectureUnableTime.updateDayOfWeek(request.getDayOfWeek());
    }

    if(request.getStartPeriodId() != null || request.getEndPeriodId() != null){
      updatePeriodRange(lectureUnableTime, request.getStartPeriodId(), request.getEndPeriodId());
    }

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      lectureUnableTime.updateNote(note);
    }

    return lectureUnableTimeJpaRepository.save(lectureUnableTime).getLectureUnableTimeId();
  }

  private void updatePeriodRange(LectureUnableTime lectureUnableTime, Long startPeriodId, Long endPeriodId){
    Period startPeriod = lectureUnableTime.getPeriodRange().getStartPeriod();
    Period endPeriod = lectureUnableTime.getPeriodRange().getEndPeriod();

    if(startPeriodId != null){
      startPeriod = periodJpaRepository
          .findById(startPeriodId)
          .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
    }

    if(endPeriodId != null){
      endPeriod = periodJpaRepository
          .findById(endPeriodId)
          .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
    }

    lectureUnableTime.updatePeriodRange(new PeriodRange(startPeriod, endPeriod));
  }

}
