package com.se.apiserver.v1.teacher.domain.usecase.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.domain.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherReadDto;
import com.se.apiserver.v1.teacher.infra.repository.ParticipatedTeacherJpaRepository;
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
public class ParticipatedTeacherReadUseCaseTest {

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  ParticipatedTeacherReadUseCase participatedTeacherReadUseCase;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Test
  void 참여_교원_조회_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .department("컴퓨터소프트웨어공학")
        .type(TeacherType.FULL_PROFESSOR)
        .build());

    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    ParticipatedTeacher participatedTeacher = participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .teacher(teacher)
        .timeTable(timeTable)
        .build());

    // When
    ParticipatedTeacherReadDto.Response response = participatedTeacherReadUseCase.read(participatedTeacher.getParticipatedTeacherId());

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
      participatedTeacherReadUseCase.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_전체_조회_성공(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .department("컴퓨터소프트웨어공학")
        .type(TeacherType.FULL_PROFESSOR)
        .build());

    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .teacher(teacher)
        .timeTable(timeTable)
        .build());

    Teacher teacher2 = teacherJpaRepository.save(Teacher.builder()
        .name("고길동")
        .department("컴퓨터소프트웨어공학")
        .type(TeacherType.FULL_PROFESSOR)
        .build());

    participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .teacher(teacher2)
        .timeTable(timeTable)
        .build());

    // When
    PageImpl responses = participatedTeacherReadUseCase.readAllByTimeTableId(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
  }
}
