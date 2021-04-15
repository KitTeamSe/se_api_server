package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.application.dto.TeacherCreateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherCreateService {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public Long create(TeacherCreateDto.Request request){
    Teacher teacher = new Teacher(request.getName(), request.getType(),
        request.getDepartment(), false, request.getNote());

    return teacherJpaRepository.save(teacher).getTeacherId();
  }
}
