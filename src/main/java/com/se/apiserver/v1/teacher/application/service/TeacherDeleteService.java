package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherDeleteService {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public void delete(Long teacherId){
    Teacher teacher = teacherJpaRepository.findById(teacherId).orElseThrow(() ->
        new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER)
      );
    teacherJpaRepository.delete(teacher);
  }
}
