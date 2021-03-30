package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectReadUseCaseTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

//  @Autowired
//  SubjectReadUseCase subjectReadUseCase;

  @Test
  void 교과_조회_성공(){

  }
  
}
