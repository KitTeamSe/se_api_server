package com.se.apiserver.v1.deployment.application.service;

import com.se.apiserver.v1.common.domain.error.ErrorCode;
import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.deployment.application.dto.DeploymentCreateDto;
import com.se.apiserver.v1.deployment.domain.entity.Deployment;
import com.se.apiserver.v1.deployment.infra.repository.DeploymentJpaRepository;
import com.se.apiserver.v1.opensubject.application.error.OpenSubjectErrorCode;
import com.se.apiserver.v1.opensubject.domain.entity.OpenSubject;
import com.se.apiserver.v1.opensubject.infra.repository.OpenSubjectJpaRepository;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.period.application.error.PeriodErrorCode;
import com.se.apiserver.v1.period.domain.entity.Period;
import com.se.apiserver.v1.period.domain.entity.PeriodRange;
import com.se.apiserver.v1.period.infra.repository.PeriodJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeploymentCreateService {

  private final DeploymentJpaRepository deploymentJpaRepository;

  private final TimeTableJpaRepository timeTableJpaRepository;
  private final OpenSubjectJpaRepository openSubjectJpaRepository;
  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;
  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final PeriodJpaRepository periodJpaRepository;

  @Transactional
  public Long create(DeploymentCreateDto.Request request){

    TimeTable timeTable = findTimeTable(request.getTimeTableId());
    OpenSubject openSubject = findOpenSubject(request.getOpenSubjectId());
    UsableLectureRoom usableLectureRoom = findUsableLectureRoom(request.getUsableLectureRoomId());
    ParticipatedTeacher participatedTeacher = findParticipatedTeacher(request.getParticipatedTeacherId());
    Period startPeriod = findPeriod(request.getStartPeriodId());
    Period endPeriod = findPeriod(request.getEndPeriodId());

    Deployment deployment = Deployment.builder()
        .timeTable(timeTable)
        .openSubject(openSubject)
        .usableLectureRoom(usableLectureRoom)
        .participatedTeacherId(participatedTeacher)
        .dayOfWeek(request.getDayOfWeek())
        .division(request.getDivision())
        .periodRange(new PeriodRange(startPeriod, endPeriod))
        .build();

    deploymentJpaRepository.save(deployment);

    return deployment.getDeploymentId();
  }

  private TimeTable findTimeTable(Long id){
    return timeTableJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));
  }

  private OpenSubject findOpenSubject(Long id){
    return openSubjectJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(OpenSubjectErrorCode.NO_SUCH_OPEN_SUBJECT));
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

}
