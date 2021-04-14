package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.infra.dto.PageRequest;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.application.dto.DeploymentReadDto;
import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.subject.domain.entity.SubjectType;
import com.se.apiserver.v1.subject.infra.repository.SubjectJpaRepository;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.entity.TeacherType;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.entity.TimeTableStatus;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeploymentReadServiceTest {

  @Autowired
  DeploymentJpaRepository deploymentJpaRepository;

  @Autowired
  DeploymentReadService deploymentReadService;

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
  void 배치_조회_성공(){
    // Given
    TimeTable timeTable = createTimeTable("테스트 시간표 1");

    Subject subject = createSubject("전자공학개론", "GE00013");
    OpenSubject openSubject = createOpenSubject(timeTable, subject);
    Division division = createDivision(openSubject);

    LectureRoom lectureRoom = createLectureRoom("BVS", 101);
    UsableLectureRoom usableLectureRoom = createUsableLectureRoom(timeTable, lectureRoom);

    Teacher teacher = createTeacher("홍길동");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");

    Deployment deployment = deploymentJpaRepository.save(Deployment.builder()
        .timeTable(timeTable)
        .division(division)
        .usableLectureRoom(usableLectureRoom)
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.FRIDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    Long id = deployment.getDeploymentId();

    // When
    DeploymentReadDto.Response response = deploymentReadService.read(id);

    // Then
    Assertions.assertThat(response.getTimeTableId()).isEqualTo(timeTable.getTimeTableId());
    Assertions.assertThat(response).isEqualTo(DeploymentReadDto.Response.fromEntity(deployment));
  }

  @Test
  void 배치_조회_존재하지_않는_배치_실패(){
    Long id = 123456L;

    // When
    Assertions.assertThatThrownBy(() -> {
      deploymentReadService.read(id);
    }).isInstanceOf(BusinessException.class).hasMessage(DeploymentErrorCode.NO_SUCH_DEPLOYMENT.getMessage());
  }

  @Test
  void 배치_전체_조회_성공(){
    TimeTable timeTable = createTimeTable("테스트 시간표 1");

    Subject subject = createSubject("전자공학개론", "GE00013");
    OpenSubject openSubject = createOpenSubject(timeTable, subject);
    Division division = createDivision(openSubject);

    LectureRoom lectureRoom = createLectureRoom("BVS", 101);
    UsableLectureRoom usableLectureRoom = createUsableLectureRoom(timeTable, lectureRoom);

    Teacher teacher = createTeacher("홍길동");
    ParticipatedTeacher participatedTeacher = createParticipatedTeacher(teacher, timeTable);

    Period startPeriod = getPeriod("1");
    Period endPeriod = getPeriod("2");

    deploymentJpaRepository.save(Deployment.builder()
        .timeTable(timeTable)
        .division(division)
        .usableLectureRoom(usableLectureRoom)
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.FRIDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    deploymentJpaRepository.save(Deployment.builder()
        .timeTable(timeTable)
        .division(division)
        .usableLectureRoom(usableLectureRoom)
        .participatedTeacher(participatedTeacher)
        .dayOfWeek(DayOfWeek.MONDAY)
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build());

    PageImpl responses = deploymentReadService.readAllByTimeTableId(PageRequest.builder()
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

  private Subject createSubject(String name, String code){
    return subjectJpaRepository.save(Subject.builder()
        .name(name)
        .code(code)
        .curriculum("컴퓨터소프트웨어공학")
        .grade(1)
        .credit(3)
        .semester(2)
        .type(SubjectType.MAJOR)
        .autoCreated(false)
        .build());
  }

  private OpenSubject createOpenSubject(TimeTable timeTable, Subject subject){
    return openSubjectJpaRepository.save(OpenSubject.builder()
        .timeTable(timeTable)
        .subject(subject)
        .numberOfDivision(1)
        .teachingTimePerWeek(subject.getCredit())
        .autoCreated(false)
        .build());
  }

  private Division createDivision(OpenSubject openSubject){
    return divisionJpaRepository.save(Division.builder()
        .openSubject(openSubject)
        .deployedTeachingTime(0)
        .build());
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
