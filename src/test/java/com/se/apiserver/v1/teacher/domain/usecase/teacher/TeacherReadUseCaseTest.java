package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.domain.usecase.teacher.TeacherReadUseCase;
import com.se.apiserver.v1.teacher.infra.dto.teacher.TeacherReadDto;
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
public class TeacherReadUseCaseTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherReadUseCase teacherReadUseCase;

  @Test
  void 교원_조회_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());

    // When
    TeacherReadDto.Response response = teacherReadUseCase.read(teacher.getTeacherId());

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
      teacherReadUseCase.read(id);
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
    PageImpl responses = teacherReadUseCase.readAll(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
