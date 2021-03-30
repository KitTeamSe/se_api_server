package com.se.apiserver.v1.teacher.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.TeacherReadDto;
import com.se.apiserver.v1.teacher.infra.dto.TeacherUpdateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherUpdateUseCase {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public TeacherReadDto.Response update(TeacherUpdateDto.Request request){
    Teacher teacher = teacherJpaRepository.findById(request.getTeacherId()).orElseThrow(() ->
      new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER)
    );

    if(request.getName() != null)
      teacher.updateName(request.getName());

    if(request.getType() != null)
      teacher.updateType(request.getType());

    if(request.getDepartment() != null)
      teacher.updateDepartment(request.getDepartment());

    teacherJpaRepository.save(teacher);

    return TeacherReadDto.Response.fromEntity(teacher);
  }
}
