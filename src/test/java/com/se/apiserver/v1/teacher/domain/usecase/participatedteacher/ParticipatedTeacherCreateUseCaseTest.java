package com.se.apiserver.v1.teacher.domain.usecase.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.domain.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherCreateDto;
import com.se.apiserver.v1.teacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.domain.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ParticipatedTeacherCreateUseCaseTest {

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  ParticipatedTeacherCreateUseCase participatedTeacherCreateUseCase;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Test
  void 참여_교원_생성_성공(){
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

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .teacherId(teacher.getTeacherId())
        .timeTableId(timeTable.getTimeTableId())
        .build();

    // When
    Long id = participatedTeacherCreateUseCase.create(request);

    // Then
    Assertions.assertThat(participatedTeacherJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 참여_교원_생성_교원이_이미_참여중_실패(){
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

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .teacherId(teacher.getTeacherId())
        .timeTableId(timeTable.getTimeTableId())
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateUseCase.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(ParticipatedTeacherErrorCode.DUPLICATED_PARTICIPATED_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_생성_존재하지_않는_교원_실패(){
    // Given
    Long teacherId = 1666L;

    TimeTable timeTable = timeTableJpaRepository.save(TimeTable.builder()
        .name("테스트 시간표 1")
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .teacherId(teacherId)
        .timeTableId(timeTable.getTimeTableId())
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateUseCase.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_생성_존재하지_않는_시간표_실패(){
    // Given
    Teacher teacher = teacherJpaRepository.save(Teacher.builder()
        .name("홍길동")
        .department("컴퓨터소프트웨어공학")
        .type(TeacherType.FULL_PROFESSOR)
        .build());

    Long timeTableId = 17777L;

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .teacherId(teacher.getTeacherId())
        .timeTableId(timeTableId)
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateUseCase.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

}
