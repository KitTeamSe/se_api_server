package com.se.apiserver.v1.participatedteacher.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.participatedteacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.participatedteacher.application.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.application.error.TeacherErrorCode;
import com.se.apiserver.v1.participatedteacher.application.dto.ParticipatedTeacherCreateDto;
import com.se.apiserver.v1.participatedteacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipatedTeacherCreateService {

  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final TeacherJpaRepository teacherJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long create(ParticipatedTeacherCreateDto.Request request){

    Teacher teacher = teacherJpaRepository.findById(request.getTeacherId())
        .orElseThrow(() -> new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER));

    TimeTable timeTable = timeTableJpaRepository.findById(request.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    if(participatedTeacherJpaRepository
        .findByTimeTableAndTeacher(timeTable, teacher)
        .isPresent()){
      throw new BusinessException(ParticipatedTeacherErrorCode.DUPLICATED_PARTICIPATED_TEACHER);
    }

    ParticipatedTeacher participatedTeacher = ParticipatedTeacher.builder()
        .teacher(teacher)
        .timeTable(timeTable)
        .build();

    participatedTeacherJpaRepository.save(participatedTeacher);

    return participatedTeacher.getParticipatedTeacherId();
  }
}
