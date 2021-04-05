package com.se.apiserver.v1.teacher.application.service.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.teacher.application.service.TeacherReadService;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.application.dto.TeacherReadDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherReadServiceTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherReadService teacherReadService;

  @Test
  void 교원_조회_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());

    // When
    TeacherReadDto.Response response = teacherReadService.read(teacher.getTeacherId());

    // Then
    Assertions.assertThat(teacher.getName()).isEqualTo(response.getName());
    Assertions.assertThat(teacher.getType()).isEqualTo(response.getType());
    Assertions.assertThat(teacher.getDepartment()).isEqualTo(response.getDepartment());
  }

  @Test
  void 교원_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      teacherReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }

  @Test
  void 교원_전체_조회_성공(){
    // Given
    teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());

    teacherJpaRepository.save(Teacher.builder()
        .name("도길동")
        .type(TeacherType.ASSISTANT)
        .department("기계공학")
        .build());

    // When
    PageImpl responses = teacherReadService.readAll(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
