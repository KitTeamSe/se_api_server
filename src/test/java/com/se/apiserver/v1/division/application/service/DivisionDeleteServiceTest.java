package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
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
public class DivisionDeleteServiceTest {

  @Autowired
  DivisionDeleteService divisionDeleteService;

  @Autowired
  DivisionJpaRepository divisionJpaRepository;

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 분반_삭제_성공(){
    // Given
    TimeTable timeTable = createTimeTable("개설_교과_생성_성공 테스트 시간표 1");

    Subject subject = createSubject("D", "ASDASD");

    OpenSubject openSubject = createOpenSubject(timeTable, subject);

    Division division = divisionJpaRepository.save(Division.builder()
        .openSubject(openSubject)
        .deployedTeachingTime(0)
        .build());

    Long id = division.getDivisionId();

    // When
    divisionDeleteService.delete(id);

    // Then
    Assertions.assertThat(divisionJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 분반_삭제_존재하지_않는_분반_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      divisionDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(DivisionErrorCode.NO_SUCH_DIVISION.getMessage());
  }

  private TimeTable createTimeTable(String name){
    return timeTableJpaRepository.save(TimeTable.builder()
        .name(name)
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());
  }

  private Subject createSubject(String name, String code){
    return subjectJpaRepository.save(Subject.builder()
        .name(name)
        .code(code)
        .curriculum("컴퓨터소프트웨어공학")
        .type(SubjectType.MAJOR)
        .credit(3)
        .semester(1)
        .grade(1)
        .autoCreated(false)
        .build());
  }

  private OpenSubject createOpenSubject(TimeTable timeTable, Subject subject){
    return openSubjectJpaRepository.save(OpenSubject.builder()
        .timeTable(timeTable)
        .subject(subject)
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .autoCreated(false)
        .build());
  }
}
