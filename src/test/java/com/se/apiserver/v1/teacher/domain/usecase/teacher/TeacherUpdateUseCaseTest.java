package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.domain.usecase.teacher.TeacherUpdateUseCase;
import com.se.apiserver.v1.teacher.infra.dto.teacher.TeacherReadDto;
import com.se.apiserver.v1.teacher.infra.dto.teacher.TeacherUpdateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherUpdateUseCaseTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherUpdateUseCase teacherUpdateUseCase;

  @Test
  void 교원_수정_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());

    Long id = teacher.getTeacherId();

    // When
    TeacherUpdateDto.Request request = TeacherUpdateDto.Request.builder()
        .teacherId(id)
        .name("고길동")
        .type(TeacherType.ASSISTANT)
        .department("기계공학")
        .build();

    TeacherReadDto.Response response = teacherUpdateUseCase.update(request);

    // Then
    Assertions.assertThat(teacher.getName()).isEqualTo(response.getName());
    Assertions.assertThat(teacher.getType()).isEqualTo(response.getType());
    Assertions.assertThat(teacher.getDepartment()).isEqualTo(response.getDepartment());
  }

  @Test
  void 교원_수정_존재하지_않는_교원_실패(){
    // Given
    Long id = 7777L;

    // When
    TeacherUpdateDto.Request request = TeacherUpdateDto.Request.builder()
        .teacherId(id)
        .name("고길동")
        .type(TeacherType.ASSISTANT)
        .department("기계공학")
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      teacherUpdateUseCase.update(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }
}
