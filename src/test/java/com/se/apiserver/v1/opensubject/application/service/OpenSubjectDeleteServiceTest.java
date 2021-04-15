package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.subject.application.service.SubjectCreateServiceTest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OpenSubjectDeleteServiceTest {

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  OpenSubjectDeleteService openSubjectDeleteService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 개설_교과_삭제_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");

    OpenSubject openSubject = OpenSubjectCreateServiceTest.createOpenSubject(openSubjectJpaRepository, timeTable, subject, 3);

    Long id = openSubject.getOpenSubjectId();

    // When
    openSubjectDeleteService.delete(id);

    // Then
    Assertions.assertThat(openSubjectJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 개설_교과_삭제_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      openSubjectDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT.getMessage());
  }
}
