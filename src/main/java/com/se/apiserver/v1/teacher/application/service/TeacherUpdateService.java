package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.application.dto.TeacherReadDto;
import com.se.apiserver.v1.teacher.application.dto.TeacherUpdateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherUpdateService {

  private final TeacherJpaRepository teacherJpaRepository;

  @Transactional
  public Long update(TeacherUpdateDto.Request request){
    Teacher teacher = teacherJpaRepository.findById(request.getTeacherId()).orElseThrow(() ->
      new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER)
    );

    if(request.getName() != null)
      teacher.updateName(request.getName());

    if(request.getType() != null)
      teacher.updateType(request.getType());

    if(request.getDepartment() != null)
      teacher.updateDepartment(request.getDepartment());

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      teacher.updateNote(note);
    }

    return teacherJpaRepository.save(teacher).getTeacherId();
  }
}
