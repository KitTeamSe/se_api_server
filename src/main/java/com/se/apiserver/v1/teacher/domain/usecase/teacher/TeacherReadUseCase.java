package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.teacher.TeacherReadDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeacherReadUseCase {

  private final TeacherJpaRepository teacherJpaRepository;

  public TeacherReadDto.Response read(Long id){
    Teacher teacher = teacherJpaRepository.
        findById(id)
        .orElseThrow(() -> new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER));
    return TeacherReadDto.Response.fromEntity(teacher);
  }

  public PageImpl readAll(Pageable pageable){
    Page<Teacher> all = teacherJpaRepository.findAll(pageable);
    List<TeacherReadDto.Response> responseList = all
        .stream()
        .map(t -> TeacherReadDto.Response.fromEntity(t))
        .collect(Collectors.toList());
    return new PageImpl(responseList, all.getPageable(), all.getTotalElements());
  }
}
