package com.se.apiserver.v1.participatedteacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherCreateService;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.application.service.TeacherCreateServiceTest;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherCreateDto;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ParticipatedTeacherCreateServiceTest {

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  ParticipatedTeacherCreateService participatedTeacherCreateService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Test
  void 참여_교원_생성_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository,"홍길동");

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .teacherId(teacher.getTeacherId())
        .build();

    // When
    Long id = participatedTeacherCreateService.create(request);

    // Then
    Assertions.assertThat(participatedTeacherJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 참여_교원_생성_교원이_이미_참여중_실패(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository,"홍길동");

    createParticipatedTeacher(participatedTeacherJpaRepository, timeTable, teacher);

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .teacherId(teacher.getTeacherId())
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(ParticipatedTeacherErrorCode.DUPLICATED_PARTICIPATED_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_생성_존재하지_않는_교원_실패(){
    // Given
    Long teacherId = 1666L;

    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .teacherId(teacherId)
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TeacherErrorCode.NO_SUCH_TEACHER.getMessage());
  }

  @Test
  void 참여_교원_생성_존재하지_않는_시간표_실패(){
    // Given
    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository,"홍길동");

    Long timeTableId = 17777L;

    ParticipatedTeacherCreateDto.Request request = ParticipatedTeacherCreateDto.Request.builder()
        .timeTableId(timeTableId)
        .teacherId(teacher.getTeacherId())
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherCreateService.create(request);
    }).isInstanceOf(BusinessException.class).hasMessage(TimeTableErrorCode.NO_SUCH_TIME_TABLE.getMessage());
  }

  public static ParticipatedTeacher createParticipatedTeacher(ParticipatedTeacherJpaRepository participatedTeacherJpaRepository,
      TimeTable timeTable, Teacher teacher){
    return participatedTeacherJpaRepository.save(new ParticipatedTeacher(timeTable, teacher));
  }
}
