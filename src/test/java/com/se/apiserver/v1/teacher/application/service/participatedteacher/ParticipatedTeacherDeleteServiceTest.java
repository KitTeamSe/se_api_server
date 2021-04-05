package com.se.apiserver.v1.teacher.application.service.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherDeleteService;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
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
public class ParticipatedTeacherDeleteServiceTest {

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  ParticipatedTeacherDeleteService participatedTeacherDeleteService;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Test
  void 참여_교원_삭제_성공(){
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

    Long id = participatedTeacher.getParticipatedTeacherId();

    // When
    participatedTeacherDeleteService.delete(id);

    // Then
    Assertions.assertThat(participatedTeacherJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 참여_교원_삭제_실패(){
    // Given
    Long id = 7777L;

    // When
    // Then
    Assertions.assertThatThrownBy(() ->{
      participatedTeacherDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER.getMessage());
  }
}
