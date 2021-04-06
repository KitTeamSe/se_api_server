package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectReadDto;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OpenSubjectReadServiceTest {

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  OpenSubjectReadService openSubjectReadService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 개설_교과_조회_성공(){
    // Given
    TimeTable timeTable = createTimeTable("개설_교과_조회_성공 테스트 시간표");

    Subject subject = createSubject("테스트 교과", "DE00002");

    OpenSubject openSubject = openSubjectJpaRepository.save(OpenSubject.builder()
        .timeTable(timeTable)
        .subject(subject)
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .build());

    // When
    OpenSubjectReadDto.Response response = openSubjectReadService.read(openSubject.getOpenSubjectId());

    // Then
    Assertions.assertThat(timeTable.getTimeTableId()).isEqualTo(response.getTimeTableId());
    Assertions.assertThat(subject.getSubjectId()).isEqualTo(response.getSubjectId());
  }

  @Test
  void 개설_교과_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      openSubjectReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT.getMessage());
  }

  @Test
  void 개설_교과_전체_조회_성공(){
    // Given
    TimeTable timeTable = createTimeTable("개설_교과_전체_조회_성공 테스트 시간표");

    Subject subject = createSubject("테스트 교과", "CS00003");

    openSubjectJpaRepository.save(OpenSubject.builder()
        .timeTable(timeTable)
        .subject(subject)
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .build());

    Subject subject2 = createSubject("테스트 교과 2", "CS00004");

    openSubjectJpaRepository.save(OpenSubject.builder()
        .timeTable(timeTable)
        .subject(subject2)
        .numberOfDivision(1)
        .teachingTimePerWeek(subject2.getCredit())
        .build());

    // When
    PageImpl responses = openSubjectReadService.readAllByTimeTableId(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of(), timeTable.getTimeTableId());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
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
        .build());
  }
}
