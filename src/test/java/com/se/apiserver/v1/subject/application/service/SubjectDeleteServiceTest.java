package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectDeleteServiceTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  SubjectDeleteService subjectDeleteService;

  @Test
  void 교과_삭제_성공(){
    // Given
    Subject subject = SubjectCreateServiceTest.createSubject(subjectJpaRepository, "전자공학개론", "GE00013");

    Long id = subject.getSubjectId();

    // When
    subjectDeleteService.delete(id);

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
      subjectDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.NO_SUCH_SUBJECT.getMessage());
  }
}
