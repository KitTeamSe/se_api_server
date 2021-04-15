package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.lectureroom.application.service.LectureRoomCreateServiceTest;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.opensubject.application.service.OpenSubjectCreateServiceTest;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.participatedteacher.application.service.ParticipatedTeacherCreateServiceTest;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.application.service.PeriodCreateServiceTest;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.application.service.SubjectCreateServiceTest;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.teacher.application.service.TeacherCreateServiceTest;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.application.service.TimeTableCreateServiceTest;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.service.UsableLectureRoomCreateServiceTest;
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

    LectureRoom lectureRoom = LectureRoomCreateServiceTest
        .createLectureRoom(lectureRoomJpaRepository, "BVS", 101);
    UsableLectureRoom usableLectureRoom = UsableLectureRoomCreateServiceTest
        .createUsableLectureRoom(usableLectureRoomJpaRepository, timeTable, lectureRoom);

    Teacher teacher = TeacherCreateServiceTest.createTeacher(teacherJpaRepository, "홍길동 1");
    ParticipatedTeacher participatedTeacher = ParticipatedTeacherCreateServiceTest
        .createParticipatedTeacher(participatedTeacherJpaRepository, timeTable, teacher);

    Period startPeriod = PeriodCreateServiceTest.getPeriod(periodJpaRepository,"1");
    Period endPeriod = PeriodCreateServiceTest.getPeriod(periodJpaRepository,"2");

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

  public static Deployment createDeployment(DeploymentJpaRepository deploymentJpaRepository,
      TimeTable timeTable, Division division, UsableLectureRoom usableLectureRoom,
      ParticipatedTeacher participatedTeacher, DayOfWeek dayOfWeek, Period startPeriod, Period endPeriod){
    return deploymentJpaRepository.save(new Deployment(
        timeTable,
        division,
        usableLectureRoom,
        participatedTeacher,
        dayOfWeek,
        new PeriodRange(startPeriod, endPeriod)));
  }
}
