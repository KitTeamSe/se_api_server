package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectUpdateUseCaseTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

//  @Autowired
//  SubjectUpdateUseCase subjectUpdateUseCase;

  @Test
  void 교과_수정_성공(){

  }
}
