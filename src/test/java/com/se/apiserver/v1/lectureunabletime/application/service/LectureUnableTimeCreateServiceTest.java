package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeCreateDto;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
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
public class LectureUnableTimeCreateServiceTest {

  @Autowired
  LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;

  @Autowired
  LectureUnableTimeCreateService lectureUnableTimeCreateService;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Test
  void 강의_불가_시간_추가_성공(){
    // Given
    Teacher teacher = createTeacher("홍길동");
    TimeTable timeTable = createTimeTable("테스트 시간표 1");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");


    // 테스트 시간표 1에 소속된 홍길동 1 교원은
    // 테스트 시간표 1에서 금요일 1교시 ~ 2교시는 수업이 불가능하다. 는 예시
    LectureUnableTimeCreateDto.Request request = LectureUnableTimeCreateDto.Request.builder()
        .participatedTeacherId(participatedTeacher.getParticipatedTeacherId())
        .dayOfWeek(DayOfWeek.FRIDAY)
        .startPeriodId(startPeriod.getPeriodId())
        .endPeriodId(endPeriod.getPeriodId())
        .build();

    // When
    Long id = lectureUnableTimeCreateService.create(request);

    // Then
    Assertions.assertThat(lectureUnableTimeJpaRepository.findById(id).isPresent()).isEqualTo(true);
  }

  @Test
  void 강의_불가_시간_추가_존재하지_않는_참여_교원_실패(){
    // Given
    Long participatedTeacherId = 132123L;

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");


    // 테스트 시간표 1에 소속된 홍길동 1 교원은
    // 테스트 시간표 1에서 금요일 1교시 ~ 2교시는 수업이 불가능하다. 는 예시
    LectureUnableTimeCreateDto.Request request = LectureUnableTimeCreateDto.Request.builder()
        .participatedTeacherId(participatedTeacherId)
        .dayOfWeek(DayOfWeek.FRIDAY)
        .startPeriodId(startPeriod.getPeriodId())
        .endPeriodId(endPeriod.getPeriodId())
        .build();

    // When

    // Then
    Assertions.assertThatThrownBy(()->
        lectureUnableTimeCreateService.create(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER.getMessage());
  }

  @Test
  void 강의_불가_시간_추가_시작_교시_종료_교시_교차_실패(){
    // Given
    Teacher teacher = createTeacher("홍길동");
    TimeTable timeTable = createTimeTable("테스트 시간표 1");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("2");
    Period endPeriod = getPeriod("1");


    // 테스트 시간표 1에 소속된 홍길동 1 교원은
    // 테스트 시간표 1에서 금요일 2교시 ~ 1교시는 수업이 불가능하다. 는 예시
    LectureUnableTimeCreateDto.Request request = LectureUnableTimeCreateDto.Request.builder()
        .participatedTeacherId(participatedTeacher.getParticipatedTeacherId())
        .dayOfWeek(DayOfWeek.FRIDAY)
        .startPeriodId(startPeriod.getPeriodId())
        .endPeriodId(endPeriod.getPeriodId())
        .build();

    // When
    // Then
    Assertions.assertThatThrownBy(()->
        lectureUnableTimeCreateService.create(request))
        .isInstanceOf(BusinessException.class)
        .hasMessage(LectureUnableTimeErrorCode.CROSSING_START_END_PERIOD.getMessage());
  }

  private Teacher createTeacher(String name){
    return teacherJpaRepository.save(Teacher.builder()
        .name(name)
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .build());
  }

  private TimeTable createTimeTable(String name){
    return timeTableJpaRepository.save(TimeTable.builder()
        .name(name)
        .year(2021)
        .semester(2)
        .status(TimeTableStatus.CREATED)
        .build());
  }

  private ParticipatedTeacher createParticipatedTeacher(Teacher teacher, TimeTable timeTable){
    return participatedTeacherJpaRepository.save(ParticipatedTeacher.builder()
        .teacher(teacher)
        .timeTable(timeTable)
        .build());
  }

  private Period getPeriod(String name){
    return periodJpaRepository
        .findByName(name)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
  }

}
