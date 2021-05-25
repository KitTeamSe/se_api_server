package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.application.error.DeploymentErrorCode;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.domain.entity.DeploymentAlertMessage;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.division.application.error.DivisionErrorCode;
import com.se.apiserver.v1.division.domain.entity.Division;
import com.se.apiserver.v1.division.infra.repository.DivisionJpaRepository;
import com.se.apiserver.v1.lectureunabletime.domain.entity.DayOfWeek;
import com.se.apiserver.v1.lectureunabletime.domain.entity.LectureUnableTime;
import com.se.apiserver.v1.lectureunabletime.infra.repository.LectureUnableTimeJpaRepository;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.subject.domain.entity.Subject;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeploymentCreateService {

  private final DeploymentJpaRepository deploymentJpaRepository;

  private final TimeTableJpaRepository timeTableJpaRepository;
  private final DivisionJpaRepository divisionJpaRepository;
  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;
  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;
  private final LectureUnableTimeJpaRepository lectureUnableTimeJpaRepository;

  @Transactional
  public DeploymentCreateDto.Resposne create(DeploymentCreateDto.Request request){
    // (필수) 정보 fetch
    TimeTable timeTable = findTimeTable(request.getTimeTableId());
    Division division = findDivision(request.getDivisionId());          // 분반이 유효한가?
    UsableLectureRoom usableLectureRoom = findUsableLectureRoom(request.getUsableLectureRoomId());
    ParticipatedTeacher participatedTeacher = findParticipatedTeacher(request.getParticipatedTeacherId());
    Period startPeriod = findPeriod(request.getStartPeriodId());
    Period endPeriod = findPeriod(request.getEndPeriodId());
    PeriodRange periodRange = new PeriodRange(startPeriod, endPeriod);

    return saveDeployment(timeTable, division, usableLectureRoom, participatedTeacher, periodRange, request.getDayOfWeek(), false, null);
  }

  public DeploymentCreateDto.Resposne saveDeployment(TimeTable timeTable, Division division, UsableLectureRoom usableLectureRoom,
      ParticipatedTeacher participatedTeacher, PeriodRange periodRange, DayOfWeek dayOfWeek, Boolean autoCreated, String note){

    DeploymentAlertMessage alertMessage = buildAlertMessage(timeTable, division, usableLectureRoom, participatedTeacher, periodRange, dayOfWeek);

    Deployment deployment = new Deployment(
        timeTable,
        division,
        usableLectureRoom,
        participatedTeacher,
        dayOfWeek,
        periodRange, autoCreated, note);

    deploymentJpaRepository.save(deployment);
    updateDeployedTeachingTime(division, division.getDeployedTeachingTime() + periodRange.getTeachingTime());

    alertMessage = alertMessage.isEmpty() ? null : alertMessage;

    return new DeploymentCreateDto.Resposne(
        deployment.getDeploymentId(),
        alertMessage
    );
  }

  private TimeTable findTimeTable(Long id){
    return timeTableJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));
  }

  private Division findDivision(Long id){
    return divisionJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(DivisionErrorCode.NO_SUCH_DIVISION));
  }

  private UsableLectureRoom findUsableLectureRoom(Long id){
    return usableLectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(UsableLectureRoomErrorCode.NO_SUCH_USABLE_LECTURE_ROOM));
  }

  private ParticipatedTeacher findParticipatedTeacher(Long id){
    return participatedTeacherJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(ParticipatedTeacherErrorCode.NO_SUCH_PARTICIPATED_TEACHER));
  }

  private Period findPeriod(Long id){
    return periodJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(PeriodErrorCode.NO_SUCH_PERIOD));
  }

  // 교원의 강의 불가 시간 검사
  private boolean isLectureUnableTimeIncluded(List<LectureUnableTime> lectureUnableTimes, PeriodRange periodRange, DayOfWeek dayOfWeek){
    for(LectureUnableTime lectureUnableTime : lectureUnableTimes){

      if(lectureUnableTime.getDayOfWeek().equals(dayOfWeek)){
        PeriodRange unableRange = lectureUnableTime.getPeriodRange();
        if(unableRange.isOverlappedWith(periodRange))
          return false;
      }

    }
    return true;
  }

  // 교원의 강의 불가 시간 검사
  private void checkLectureUnableTime(DeploymentAlertMessage alertMessage, List<LectureUnableTime> lectureUnableTimes, PeriodRange periodRange, DayOfWeek dayOfWeek){
    if(lectureUnableTimes!= null && !lectureUnableTimes.isEmpty())
      if(!isLectureUnableTimeIncluded(lectureUnableTimes, periodRange, dayOfWeek))
        alertMessage.addAlertMessages(DeploymentErrorCode.TEACHER_LECTURE_UNABLE_TIME);
  }

  // 주간 수업 시간 초과 검사
  private void checkOverTeaching(DeploymentAlertMessage alertMessage, TimeTable timeTable, Division division, PeriodRange periodRange){
    // 배치된 주간 수업 시간 + 넣을 배치의 수업 시간 > 주간 수업 시간
    if(division.getDeployedTeachingTime() + periodRange.getTeachingTime() > division.getOpenSubject().getTeachingTimePerWeek())
      alertMessage.addAlertMessages(DeploymentErrorCode.OVER_TEACHING_PER_WEEK);
  }

  // 같은 학년이 수업 받는 중인지 검사
  private void checkSameGradeLectured(DeploymentAlertMessage alertMessage, PeriodRange periodRange, List<Deployment> deployments, int grade){
    for(Deployment deployment : deployments){
      Subject subject = deployment.getDivision().getOpenSubject().getSubject();
      if(subject.getGrade().equals(grade)){
        if(deployment.getPeriodRange().isOverlappedWith(periodRange)){
          alertMessage.addAlertMessages(DeploymentErrorCode.SAME_GRADE_LECTURED);
          break;
        }
      }
    }
  }

  // 교원이 수업 중인지 검사
  private void checkTeacherLectures(DeploymentAlertMessage alertMessage, PeriodRange periodRange, List<Deployment> deployments, ParticipatedTeacher participatedTeacher){
    for(Deployment deployment : deployments){
      if(participatedTeacher.equals(deployment.getParticipatedTeacher())){
        if(periodRange.isOverlappedWith(deployment.getPeriodRange())){
          alertMessage.addAlertMessages(DeploymentErrorCode.TEACHER_LECTURES_SAME_TIME);
          break;
        }
      }
    }
  }

  // 요일, 강의실, 교시 겹치는지 검사
  private void checkOverlap(DeploymentAlertMessage alertMessage, PeriodRange periodRange, List<Deployment> deployments,  UsableLectureRoom usableLectureRoom){
    for(Deployment deployment : deployments){
      if(usableLectureRoom.equals(deployment.getUsableLectureRoom())){
        if(periodRange.isOverlappedWith(deployment.getPeriodRange())){
          alertMessage.addAlertMessages(DeploymentErrorCode.DEPLOYMENT_OVERLAPPED);
          break;
        }
      }
    }
  }

  private void updateDeployedTeachingTime(Division division, Integer newTeachingTime){
    division.updateDeployedTeachingTime(newTeachingTime);
    divisionJpaRepository.save(division);
  }

  private DeploymentAlertMessage buildAlertMessage(TimeTable timeTable, Division division,
      UsableLectureRoom usableLectureRoom, ParticipatedTeacher participatedTeacher,
      PeriodRange periodRange, DayOfWeek dayOfWeek){

    DeploymentAlertMessage alertMessage = new DeploymentAlertMessage();
    List<LectureUnableTime> lectureUnableTimes = null;
    if(participatedTeacher.getParticipatedTeacherId() != null)
      lectureUnableTimes = lectureUnableTimeJpaRepository.findAllByParticipatedTeacher(participatedTeacher);

    // 교원 강의 불가 시간 검사
    checkLectureUnableTime(alertMessage, lectureUnableTimes, periodRange, dayOfWeek);

    // 이 배치로 인해 주간 수업 시간이 초과되는가?
    checkOverTeaching(alertMessage, timeTable, division, periodRange);

    // 시간표에 포함된 모든 배치 정보 중 요일이 같은 배치 정보 가져옴
    List<Deployment> deployments = deploymentJpaRepository.findAllByTimeTableAndDayOfWeek(timeTable, dayOfWeek);

    // 해당 학년이 동 시간대에 강의 받는 중인지 검사
    checkSameGradeLectured(alertMessage, periodRange, deployments, division.getOpenSubject().getSubject().getGrade());

    // 교원이 동 시간대에 강의중인지 검사
    checkTeacherLectures(alertMessage, periodRange, deployments, participatedTeacher);

    // 겹치는 강의가 있는지 검사
    checkOverlap(alertMessage, periodRange, deployments, usableLectureRoom);

    return alertMessage;
  }
}
