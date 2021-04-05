package com.se.apiserver.v1.participatedteacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipatedTeacherDeleteService {

  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Transactional
  public void delete(Long participatedTeacherId){
    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository
        .findById(participatedTeacherId)
        .orElseThrow(() ->
        new BusinessException(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER)
    );
    participatedTeacherJpaRepository.delete(participatedTeacher);
  }
}
