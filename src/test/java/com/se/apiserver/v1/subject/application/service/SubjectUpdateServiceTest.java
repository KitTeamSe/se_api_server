package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.application.dto.SubjectUpdateDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectUpdateServiceTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  SubjectUpdateService subjectUpdateService;

  @Test
  void 교과_수정_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00001")
        .name("과목 1")
        .grade(1)
        .semester(1)
        .credit(3)
        .autoCreated(false)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    SubjectUpdateDto.Request request = SubjectUpdateDto.Request.builder()
        .subjectId(id)
        .name("수정된 과목명")
        .build();

    subjectUpdateService.update(request);

    Subject response = subjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));

    // Then
    Assertions.assertThat(response.getName()).isEqualTo("수정된 과목명");
  }

  @Test
  void 교과_수정_코드_동일_과목명만_변경_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00001")
        .name("과목 1")
        .grade(1)
        .semester(1)
        .credit(3)
        .autoCreated(false)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    SubjectUpdateDto.Request request = SubjectUpdateDto.Request.builder()
        .subjectId(id)
        .code("CS00001")
        .name("수정된 과목명")
        .build();


    subjectUpdateService.update(request);

    Subject response = subjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));
    // Then
    Assertions.assertThat(response.getName()).isEqualTo("수정된 과목명");
  }

  @Test
  void 교과_수정_코드_변경_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00001")
        .name("과목 1")
        .grade(1)
        .semester(1)
        .credit(3)
        .autoCreated(false)
        .build()
    );

    subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("과목 2")
        .grade(1)
        .semester(2)
        .credit(4)
        .autoCreated(false)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    SubjectUpdateDto.Request request = SubjectUpdateDto.Request.builder()
        .subjectId(id)
        .code("CS00003")
        .build();

    subjectUpdateService.update(request);

    Subject response = subjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(SubjectErrorCode.NO_SUCH_SUBJECT));
    // Then
    Assertions.assertThat(response.getCode()).isEqualTo("CS00003");
  }

  @Test
  void 교과_수정_중복된_코드_실패(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00001")
        .name("과목 1")
        .grade(1)
        .semester(1)
        .credit(3)
        .autoCreated(false)
        .build()
    );

    subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("과목 2")
        .grade(1)
        .semester(2)
        .credit(4)
        .autoCreated(false)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    SubjectUpdateDto.Request request = SubjectUpdateDto.Request.builder()
        .subjectId(id)
        .code("CS00002")
        .build();

    // Then
    Assertions.assertThatThrownBy(() ->{
      subjectUpdateService.update(request);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.DUPLICATED_SUBJECT.getMessage());
  }

  @Test
  void 교과_수정_존재하지_않는_교과_실패(){
    // Given
    Long id = 7777L;

    // When
    SubjectUpdateDto.Request request = SubjectUpdateDto.Request.builder()
        .subjectId(id)
        .name("수정된 과목명")
        .build();

    // Then
    Assertions.assertThatThrownBy(() ->{
      subjectUpdateService.update(request);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.NO_SUCH_SUBJECT.getMessage());
  }
}
