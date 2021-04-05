package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherDeleteUseCase {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public void delete(Long teacherId){
    Teacher teacher = teacherJpaRepository.findById(teacherId).orElseThrow(() ->
        new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER)
      );
    teacherJpaRepository.delete(teacher);
  }
}
