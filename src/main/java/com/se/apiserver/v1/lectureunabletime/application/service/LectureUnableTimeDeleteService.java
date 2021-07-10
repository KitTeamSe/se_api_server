package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureUnableTimeDeleteService {

  private final LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;

  @Transactional
  public void delete(Long participatedTeacherId){
    LectureUnableTime lectureUnableTime = lectureUnableTimeJpaRepository
        .findById(participatedTeacherId)
        .orElseThrow(() ->
            new BusinessException(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME)
        );
    lectureUnableTimeJpaRepository.delete(lectureUnableTime);
  }
}
