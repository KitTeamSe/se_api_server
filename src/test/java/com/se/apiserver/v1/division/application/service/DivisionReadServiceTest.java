package com.se.apiserver.v1.division.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto;
import com.se.apiserver.v1.division.application.dto.DivisionReadDto.Response;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateServiceTest;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.subject.application.service.SubjectCreateServiceTest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DivisionReadServiceTest {

  @Autowired
  DivisionReadService divisionReadService;

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Test
  void 분반_조회_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");
    OpenSubject openSubject = OpenSubjectCreateServiceTest
        .createOpenSubject(openSubjectJpaRepository, timeTable, subject, 3);

    Division division = openSubject.getDivisions().get(0);

    Long id = division.getDivisionId();

    // When
    DivisionReadDto.Response response = divisionReadService.read(id);

    // Then
    Assertions.assertThat(response.getDeployedTeachingTime()).isEqualTo(division.getDeployedTeachingTime());
    Assertions.assertThat(response).isEqualTo(DivisionReadDto.Response.fromEntity(division));
  }

  @Test
  void 분반_조회_존재하지_않는_분반_실패(){
    Long id = 123456L;

    // When
    Assertions.assertThatThrownBy(() -> {
      divisionReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(DivisionErrorCode.NO_SUCH_DIVISION.getMessage());
  }

  @Test
  void 분반_전체_조회_성공(){
    TimeTable timeTable = TimeTableCreateServiceTest
        .createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest
        .createSubject(subjectJpaRepository, "전자공학개론", "GE00013");
    OpenSubject openSubject = OpenSubjectCreateServiceTest
        .createOpenSubject(openSubjectJpaRepository, timeTable, subject, 3);

    List<Response> responses = divisionReadService.readAllByOpenSubjectId(openSubject.getOpenSubjectId());

    // Then
    Assertions.assertThat(responses.size()).isEqualTo(3);
  }
}
