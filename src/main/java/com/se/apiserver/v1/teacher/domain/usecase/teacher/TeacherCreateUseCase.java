package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.infra.dto.teacher.TeacherCreateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherCreateUseCase {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public Long create(TeacherCreateDto.Request request){
    Teacher teacher = Teacher.builder()
        .name(request.getName())
        .type(request.getType())
        .department(request.getDepartment())
        .build();

    teacherJpaRepository.save(teacher);

    return teacher.getTeacherId();
  }
}
