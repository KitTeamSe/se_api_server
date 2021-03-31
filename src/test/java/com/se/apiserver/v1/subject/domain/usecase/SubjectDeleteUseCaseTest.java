package com.se.apiserver.v1.subject.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.domain.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectDeleteUseCaseTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  SubjectDeleteUseCase subjectDeleteUseCase;

  @Test
  void 교과_삭제_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00001")
        .name("과목 1")
        .grade(1)
        .semester(1)
        .credit(3)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    subjectDeleteUseCase.delete(id);

    // Then
    Assertions.assertThat(subjectJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 교과_삭제_존재하지_않는_교과_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      subjectDeleteUseCase.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.NO_SUCH_SUBJECT.getMessage());
  }
}
