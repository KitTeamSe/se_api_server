package com.se.apiserver.v1.subject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.dto.SubjectReadDto;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SubjectReadServiceTest {

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  SubjectReadService subjectReadService;

  @Test
  void 교과_조회_성공(){
    // Given
    Subject subject = subjectJpaRepository.save(Subject.builder()
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .code("CS00002")
        .name("전자공학개론")
        .grade(1)
        .semester(1)
        .credit(3)
        .build()
    );

    Long id = subject.getSubjectId();

    // When
    SubjectReadDto.Response response = subjectReadService.read(id);

    // Then
    Assertions.assertThat(subject.getCurriculum()).isEqualTo(response.getCurriculum());
    Assertions.assertThat(subject.getType()).isEqualTo(response.getType());
    Assertions.assertThat(subject.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(subject.getName()).isEqualTo(response.getName());
    Assertions.assertThat(subject.getGrade()).isEqualTo(response.getGrade());
    Assertions.assertThat(subject.getSemester()).isEqualTo(response.getSemester());
    Assertions.assertThat(subject.getCredit()).isEqualTo(response.getCredit());
  }

  @Test
  void 교과_조회_실패(){
    // Given
    Long id = 777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      subjectReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.NO_SUCH_SUBJECT.getMessage());
  }

  @Test
  void 교과_전체_조회_성공(){
    // Given
    int numberOfSubjects = 4;
    createMultipleSubjects(numberOfSubjects);

    // When
    PageImpl responses = subjectReadService.readAll(PageRequest.builder()
        .size(100)
        .direction(Sort.Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(numberOfSubjects);
  }

  private void createMultipleSubjects(int count){
    for(int i = 0 ; i < count ; i++){
      subjectJpaRepository.save(Subject.builder()
          .curriculum("컴퓨터소프트웨어공학")
          .type(SubjectType.MAJOR)
          .code("CS0000" + i)
          .name("과목 " + i)
          .grade(1)
          .semester(1)
          .credit(3)
          .build()
      );
    }
  }
  
}
