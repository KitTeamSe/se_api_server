package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionCreateDto;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
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
    TimeTable timeTable = createTimeTable("개설_교과_생성_성공 테스트 시간표 1");

    Subject subject = createSubject("D", "ASDASD");

    OpenSubject openSubject = createOpenSubject(timeTable, subject);

    DivisionCreateDto.Request request = DivisionCreateDto.Request.builder()
        .openSubjectId(openSubject.getOpenSubjectId())
        .build();

    // When
    Long id = divisionCreateService.create(request);

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
