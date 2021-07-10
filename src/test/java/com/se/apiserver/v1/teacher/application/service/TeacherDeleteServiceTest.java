package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.application.service.TeacherDeleteService;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherDeleteServiceTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherDeleteService teacherDeleteService;

  @Test
  void 교원_삭제_성공(){
    // Given
    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository, "홍길동");

    Long id = teacher.getTeacherId();

    // When
    teacherDeleteService.delete(id);

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
      teacherDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }
}
