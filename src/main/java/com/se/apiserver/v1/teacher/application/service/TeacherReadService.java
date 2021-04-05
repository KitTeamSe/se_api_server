package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.application.dto.TeacherReadDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeacherReadService {

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
