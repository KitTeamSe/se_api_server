package com.se.apiserver.v1.teacher.domain.usecase.participatedteacher;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.teacher.domain.entity.ParticipatedTeacher;
import com.se.apiserver.v1.teacher.domain.entity.Teacher;
import com.se.apiserver.v1.teacher.domain.error.ParticipatedTeacherErrorCode;
import com.se.apiserver.v1.teacher.domain.error.TeacherErrorCode;
import com.se.apiserver.v1.teacher.infra.dto.participatedteacher.ParticipatedTeacherCreateDto;
import com.se.apiserver.v1.teacher.infra.repository.ParticipatedTeacherJpaRepository;
import com.se.apiserver.v1.teacher.infra.repository.TeacherJpaRepository;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.domain.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipatedTeacherCreateUseCase {

  private final ParticipatedTeacherJpaRepository participatedTeacherJpaRepository;
  private final TeacherJpaRepository teacherJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;

  @Transactional
  public Long create(ParticipatedTeacherCreateDto.Request request){

    Teacher teacher = teacherJpaRepository.findById(request.getTeacherId())
        .orElseThrow(() -> new BusinessException(TeacherErrorCode.NO_SUCH_TEACHER));

    TimeTable timeTable = timeTableJpaRepository.findById(request.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    if(participatedTeacherJpaRepository.findByTeacherIdAndTimeTableId(
        teacher.getTeacherId(),
        timeTable.getTimeTableId()).isPresent()){
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
