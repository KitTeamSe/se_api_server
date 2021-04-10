package com.se.apiserver.v1.teacher.application.service;

import com.se.apiserver.v1.teacher.application.service.TeacherCreateService;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.application.dto.TeacherCreateDto;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherCreateServiceTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TeacherCreateService teacherCreateService;

  @Test
  void 교원_생성_성공(){
    // Given
    TeacherCreateDto.Request request = TeacherCreateDto.Request.builder()
        .name("홍길동")
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .note("홍길동의 비고")
        .build();

    // When
    Long id = teacherCreateService.create(request);

    // Then
    Assertions.assertThat(teacherJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

}
