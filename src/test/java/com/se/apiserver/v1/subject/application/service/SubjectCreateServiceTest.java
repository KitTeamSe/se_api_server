package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectCreateDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectCreateServiceTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  SubjectCreateService subjectCreateService;

  @Test
  void 교과_생성_성공(){
    // Given
    SubjectCreateDto.Request request = SubjectCreateDto.Request.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .note("비고 입력")
        .build();

    // When
    Long id = subjectCreateService.create(request);

    // Then
    Assertions.assertThat(subjectJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 교과_생성_교과_중복_실패(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .note("비고 입력")
        .autoCreated(false)
        .build()
    );

    // When
    SubjectCreateDto.Request request = SubjectCreateDto.Request.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .build();

    // Then
    Assertions.assertThatThrownBy(() ->{
      subjectCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.DUPLICATED_SUBJECT.getMessage());
  }

  @Test
  void 교과_생성_교과_코드만_다름_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .autoCreated(false)
        .build()
    );

    // When
    SubjectCreateDto.Request request = SubjectCreateDto.Request.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00003")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .build();

    Long id = subjectCreateService.create(request);

    // Then
    Assertions.assertThat(subjectJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }
}
