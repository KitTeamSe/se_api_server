package com.se.apiserver.v1.participatedteacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherReadService;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherReadDto;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
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
public class ParticipatedTeacherReadServiceTest {

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  ParticipatedTeacherReadService participatedTeacherReadService;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Test
  void 참여_교원_조회_성공(){
    // Given
    TimeTable timeTable = createTimeTable("참여_교원_조회_성공 테스트 시간표 1");

    Teacher teacher = createTeacher("홍길동 1");

    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .timeTable(timeTable)
        .teacher(teacher)
        .build());

    // When
    ParticipatedTeacherReadDto.Response response = participatedTeacherReadService.read(participatedTeacher.getParticipatedTeacherId());

    // Then
    Assertions.assertThat(teacher.getTeacherId()).isEqualTo(response.getTeacherId());
    Assertions.assertThat(timeTable.getTimeTableId()).isEqualTo(response.getTimeTableId());
  }

  @Test
  void 참여_교원_조회_실패(){
    // Given
    Long id = 99999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      participatedTeacherReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_전체_조회_성공(){
    // Given
    TimeTable timeTable = createTimeTable("참여_교원_조회_성공 테스트 시간표 1");

    Teacher teacher = createTeacher("홍길동 1");

    participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .timeTable(timeTable)
        .teacher(teacher)
        .build());

    Teacher teacher2 = createTeacher("고길동 1");

    participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .timeTable(timeTable)
        .teacher(teacher2)
        .build());

    // When
    PageImpl responses = participatedTeacherReadService.readAllByTimeTableId(PageRequest.builder()
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

  private Teacher createTeacher(String name){
    return teacherJpaRepository.save(Teacher.builder()
        .name(name)
        .department("컴퓨터소프트웨어공학")
        .type(TeacherType.FULL_PROFESSOR)
        .autoCreated(false)
        .build());
  }
}
