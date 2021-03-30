package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectDeleteUseCaseTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

//  @Autowired
//  SubjectDeleteUseCase subjectDeleteUseCase;

  @Test
  void 교과_삭제_성공(){
    // Given

    // When

    // Then

  }
}