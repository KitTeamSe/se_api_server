package com.se.apiserver.v1.opensubject.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.opensubject.application.dto.OpenSubjectCreateDto;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.subject.application.error.SubjectErrorCode;
import com.se.apiserver.v1.subject.application.service.SubjectCreateServiceTest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
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
public class OpenSubjectCreateServiceTest {

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  OpenSubjectCreateService openSubjectCreateService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 개설_교과_생성_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");

    OpenSubjectCreateDto.Request request = OpenSubjectCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .subjectId(subject.getSubjectId())
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .note("개설 교과의 비고")
        .build();

    // When
    Long id = openSubjectCreateService.create(request);

    // Then
    Assertions.assertThat(openSubjectJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 개설_교과_생성_교과_이미_개설됨_실패(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");

    createOpenSubject(openSubjectJpaRepository, timeTable, subject, 1);

    OpenSubjectCreateDto.Request request = OpenSubjectCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .subjectId(subject.getSubjectId())
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .note("비고 1")
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      openSubjectCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(OpenSubjectErrorCode.DUPLICATED_OPEN_SUBJECT.getMessage());
  }

  @Test
  void 개설_교과_생성_존재하지_않는_시간표_실패(){
    // Given
    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");

    Long timeTableId = 17777L;

    OpenSubjectCreateDto.Request request = OpenSubjectCreateDto.Request.builder()
        .timeTableId(timeTableId)
        .subjectId(subject.getSubjectId())
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      openSubjectCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

  @Test
  void 개설_교과_생성_존재하지_않는_교과_실패(){
    // Given
    Long subjectId = 1666L;

    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    OpenSubjectCreateDto.Request request = OpenSubjectCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .subjectId(subjectId)
        .numberOfDivision(1)
        .teachingTimePerWeek(1)
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      openSubjectCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(SubjectErrorCode.NO_SUCH_SUBJECT.getMessage());
  }

  public static OpenSubject createOpenSubject(OpenSubjectJpaRepository openSubjectJpaRepository,
      TimeTable timeTable, Subject subject, int numberOfDivision){
    return openSubjectJpaRepository.save(new OpenSubject(
        timeTable, subject, numberOfDivision, subject.getCredit(), false));
  }
}
