package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeReadDto;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureUnableTimeReadServiceTest {

  @Autowired
  LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;

  @Autowired
  LectureUnableTimeReadService lectureUnableTimeReadService;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Test
  void 강의_불가_시간_조회_성공(){
    // Given
    Teacher teacher = createTeacher("홍길동");
    TimeTable timeTable = createTimeTable("테스트 시간표 1");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");


    // 테스트 시간표 1에 소속된 홍길동 1 교원은
    // 테스트 시간표 1에서 금요일 1교시 ~ 2교시는 수업이 불가능하다. 는 예시
    LectureUnableTime lectureUnableTime = lectureUnableTimeJpaRepository.save(LectureUnableTime.builder()
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.FRIDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    // When
    LectureUnableTimeReadDto.Response response = lectureUnableTimeReadService.read(lectureUnableTime.getLectureUnableTimeId());

    // Then
    Assertions.assertThat(participatedTeacher.getParticipatedTeacherId()).isEqualTo(response.getParticipatedTeacherId());
    Assertions.assertThat(DayOfWeek.FRIDAY).isEqualTo(response.getDayOfWeek());
    Assertions.assertThat(startPeriod.getPeriodId()).isEqualTo(response.getStartPeriodId());
    Assertions.assertThat(endPeriod.getPeriodId()).isEqualTo(response.getEndPeriodId());
  }

  @Test
  void 강의_불가_시간_조회_실패(){
    // Given
    Long id = 123123L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureUnableTimeReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME.getMessage());
  }

  @Test
  void 강의_불가_시간_전체_조회_성공(){
    // Given
    Teacher teacher = createTeacher("홍길동");
    TimeTable timeTable = createTimeTable("테스트 시간표 1");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");


    // 테스트 시간표 1에 소속된 홍길동 1 교원은
    // 테스트 시간표 1에서 금요일 1교시 ~ 2교시는 수업이 불가능하다. 는 예시
    lectureUnableTimeJpaRepository.save(LectureUnableTime.builder()
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.FRIDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    lectureUnableTimeJpaRepository.save(LectureUnableTime.builder()
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.WEDNESDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    // When
    PageImpl responses = lectureUnableTimeReadService.readAllByParticipatedTeacher(PageRequest.builder()
        .size(100)
        .direction(Direction.ASC)
        .page(1)
        .build().of(), participatedTeacher.getParticipatedTeacherId());

    // Then
    Assertions.assertThat(responses.getTotalElements()).isEqualTo(2);
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
