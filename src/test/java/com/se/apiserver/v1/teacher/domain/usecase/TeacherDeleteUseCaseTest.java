package com.se.apiserver.v1.teacher.domain.usecase;

import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TeacherDeleteUseCaseTest {

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Test
  void 교원_삭제_성공(){

  }
}
