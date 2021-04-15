package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateServiceTest;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.application.service.SubjectCreateServiceTest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeploymentCreateServiceTest {

  @Autowired
  DeploymentJpaRepository deploymentJpaRepository;

  @Autowired
  DeploymentCreateService deploymentCreateService;

  @Autowired
  TimeTableJpaRepository timeTableJpaRepository;

  @Autowired
  SubjectJpaRepository subjectJpaRepository;

  @Autowired
  OpenSubjectJpaRepository openSubjectJpaRepository;

  @Autowired
  DivisionJpaRepository divisionJpaRepository;

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;

  @Autowired
  TeacherJpaRepository teacherJpaRepository;

  @Autowired
  ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;

  @Autowired
  PeriodJpaRepository periodJpaRepository;

  @Test
  void 배치_추가_성공(){
    // Given
    TimeTable timeTable = TimeTableCreateServiceTest.createTimeTable(timeTableJpaRepository, "테스트 시간표 1");

    Subject subject = SubjectCreateServiceTest.createSubject(subjectJpaRepository, "전자공학개론", "GE00013");
    OpenSubject openSubject = OpenSubjectCreateServiceTest.createOpenSubject(openSubjectJpaRepository, timeTable, subject, 3);

    LectureRoom lectureRoom = createLectureRoom("BVS", 101);
    UsableLectureRoom usableLectureRoom = createUsableLectureRoom(timeTable, lectureRoom);

    Teacher teacher = createTeacher("홍길동");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");

    DeploymentCreateDto.Request request = DeploymentCreateDto.Request.builder()
        .timeTableId(timeTable.getTimeTableId())
        .divisionId(openSubject.getDivisions().get(0).getDivisionId())
        .usableLectureRoomId(usableLectureRoom.getUsableLectureRoomId())
        .participatedTeacherId(participatedTeacher.getParticipatedTeacherId())
        .dayOfWeek(DayOfWeek.FRIDAY)
        .startPeriodId(startPeriod.getPeriodId())
        .endPeriodId(endPeriod.getPeriodId())
        .build();

    // When
    DeploymentCreateDto.Resposne resposne = deploymentCreateService.create(request);

    // Then
    Assertions.assertThat(deploymentJpaRepository.findById(resposne.getDeploymentId()).isPresent()).isEqualTo(true);
  }

  private LectureRoom createLectureRoom(String building, Integer roomNumber){
    return lectureRoomJpaRepository.save(LectureRoom.builder()
        .building(building)
        .roomNumber(roomNumber)
        .capacity(50)
        .build());
  }

  private UsableLectureRoom createUsableLectureRoom(TimeTable timeTable, LectureRoom lectureRoom){
    return usableLectureRoomJpaRepository.save(UsableLectureRoom.builder()
        .timeTable(timeTable)
        .lectureRoom(lectureRoom)
        .build());
  }


  private Teacher createTeacher(String name){
    return teacherJpaRepository.save(Teacher.builder()
        .name(name)
        .type(TeacherType.FULL_PROFESSOR)
        .department("컴퓨터소프트웨어공학")
        .autoCreated(false)
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
