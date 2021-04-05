package com.se.apiserver.v1.teacher.domain.usecase.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.repository.ParticipatedTeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipatedTeacherDeleteUseCase {

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
