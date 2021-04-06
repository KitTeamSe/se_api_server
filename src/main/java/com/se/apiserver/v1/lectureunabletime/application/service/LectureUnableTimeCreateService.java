package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeCreateDto;
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
public class LectureUnableTimeCreateService {

  private final LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;
  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public Long create(LectureUnableTimeCreateDto.Request request){

    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository
        .findById(request.getParticipatedTeacherId())
        .orElseThrow(() -> new BusinessException(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER));

    Period startPeriod = periodJpaRepository
        .findById(request.getStartPeriodId())
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));

    Period endPeriod = periodJpaRepository
        .findById(request.getEndPeriodId())
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));

    if(isPeriodCrossing(startPeriod, endPeriod))
      throw new BusinessException(LectureUnableTimeErrorCode.CROSSING_START_END_PERIOD);

    LectureUnableTime lectureUnableTime = LectureUnableTime.builder()
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(request.getDayOfWeek())
        .startPeriod(startPeriod)
        .endPeriod(endPeriod)
        .note(request.getNote())
        .build();

    lectureUnableTimeJpaRepository.save(lectureUnableTime);

    return lectureUnableTime.getLectureUnableTimeId();
  }

  private boolean isPeriodCrossing(Period startPeriod, Period endPeriod){
    return startPeriod.getPeriodOrder() > endPeriod.getPeriodOrder();
  }
}
