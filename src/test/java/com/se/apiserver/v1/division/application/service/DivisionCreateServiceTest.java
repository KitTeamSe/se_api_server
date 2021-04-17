package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionCreateDto;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateServiceTest;
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
public class DivisionCreateServiceTest {

  @Autowired
  DivisionCreateService divisionCreateService;

  @Autowired
  DivisionJpaRepository divisionJpaRepository;

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 분반_생성_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");
    OpenSubject openSubject = OpenSubjectCreateServiceTest
        .createOpenSubject(openSubjectJpaRepository, timeTable, subject, 3);

    // When
    Long id = openSubject.getDivisions().get(0).getDivisionId();

    // Then
    Assertions.assertThat(divisionJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 분반_생성_존재하지_않는_개설_교과_실패(){
    // Given
    Long openSubjectId = 17777L;

    DivisionCreateDto.Request request = DivisionCreateDto.Request.builder()
        .openSubjectId(openSubjectId)
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      divisionCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT.getMessage());
  }

}
