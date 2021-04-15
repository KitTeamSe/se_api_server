package com.se.apiserver.v1.lectureunabletime.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherCreateServiceTest;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.lectureunabletime.application.dto.LectureUnableTimeUpdateDto;
import com.se.apiserver.v1.lectureunabletime.application.error.LectureUnableTimeErrorCode;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.teacher.application.service.TeacherCreateServiceTest;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
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
public class LectureUnableTimeUpdateServiceTest {

  @Autowired
  LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;

  @Autowired
  LectureUnableTimeUpdateService lectureUnableTimeUpdateService;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Test
  void 강의_불가_시간_수정_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");
    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository, "홍길동 1");
    ParticipatedTeacher participatedTeacher = ParticipatedTeacherCreateServiceTest
        .createParticipatedTeacher(participatedTeacherJpaRepository, timeTable, teacher);

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
    LectureUnableTimeUpdateDto.Request request = LectureUnableTimeUpdateDto.Request.builder()
        .lectureUnableTimeId(lectureUnableTime.getLectureUnableTimeId())
        .dayOfWeek(DayOfWeek.SATURDAY)
        .build();

    Long id = lectureUnableTimeUpdateService.update(request);

    // Then
    LectureUnableTime changed = lectureUnableTimeJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME));

    Assertions.assertThat(DayOfWeek.SATURDAY).isEqualTo(changed.getDayOfWeek());

  }

  @Test
  void 강의_불가_시간_수정_실패_존재하지_않는_강의_불가_시간(){
    // Given
    Long id = 7777L;

    // When
    LectureUnableTimeUpdateDto.Request request = LectureUnableTimeUpdateDto.Request.builder()
        .lectureUnableTimeId(id)
        .dayOfWeek(DayOfWeek.SATURDAY)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureUnableTimeUpdateService.update(request);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureUnableTimeErrorCode.NO_SUCH_LECTURE_UNABLE_TIME.getMessage());
  }

  private Period getPeriod(String name){
    return periodJpaRepository
        .findByName(name)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
  }
}
