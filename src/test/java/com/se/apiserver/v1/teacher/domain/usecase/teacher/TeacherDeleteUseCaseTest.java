package com.se.apiserver.v1.teacher.domain.usecase.teacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.domain.usecase.teacher.TeacherDeleteUseCase;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherDeleteUseCaseTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherDeleteUseCase teacherDeleteUseCase;

  @Test
  void 교원_삭제_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());

    Long id = teacher.getTeacherId();

    // When
    teacherDeleteUseCase.delete(id);

    // Then
    Assertions.assertThat(teacherJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 교원_삭제_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      teacherDeleteUseCase.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }
}
