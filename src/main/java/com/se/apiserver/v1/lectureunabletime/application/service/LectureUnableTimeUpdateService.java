package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeUpdateDto;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
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

    if(request.getStartPeriodId() != null){
      Period startPeriod = periodJpaRepository
          .findById(request.getStartPeriodId())
          .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
      lectureUnableTime.updateStartPeriod(startPeriod);
    }

    if(request.getEndPeriodId() != null){
      Period endPeriod = periodJpaRepository
          .findById(request.getEndPeriodId())
          .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
      lectureUnableTime.updateEndPeriod(endPeriod);
    }

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      lectureUnableTime.updateNote(note);
    }

    // 변경된 시간이 교차되는지 검사
    lectureUnableTime.validatePeriods();

    return lectureUnableTimeJpaRepository.save(lectureUnableTime).getLectureUnableTimeId();
  }

  private boolean isPeriodCrossing(Period startPeriod, Period endPeriod){
    return startPeriod.getPeriodOrder() > endPeriod.getPeriodOrder();
  }
}
