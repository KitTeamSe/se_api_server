package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.timetable.application.error.TimeTableErrorCode;
import com.se.apiserver.v1.timetable.domain.entity.TimeTable;
import com.se.apiserver.v1.timetable.infra.repository.TimeTableJpaRepository;
import com.se.apiserver.v1.usablelectureroom.application.dto.UsableLectureRoomCreateDto;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsableLectureRoomCreateService {

  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;
  private final TimeTableJpaRepository timeTableJpaRepository;
  private final LectureRoomJpaRepository lectureRoomJpaRepository;


  @Transactional
  public Long create(UsableLectureRoomCreateDto.Request request){

    TimeTable timeTable = timeTableJpaRepository.findById(request.getTimeTableId())
        .orElseThrow(() -> new BusinessException(TimeTableErrorCode.NO_SUCH_TIME_TABLE));

    LectureRoom lectureRoom = lectureRoomJpaRepository.findById(request.getLectureRoomId())
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));

    if(usableLectureRoomJpaRepository
        .findByTimeTableAndLectureRoom(timeTable, lectureRoom)
        .isPresent()){
      throw new BusinessException(UsableLectureRoomErrorCode.DUPLICATED_USABLE_LECTURE_ROOM);
    }

    UsableLectureRoom participatedTeacher = new UsableLectureRoom(timeTable, lectureRoom);

    usableLectureRoomJpaRepository.save(participatedTeacher);

    return participatedTeacher.getUsableLectureRoomId();
  }
}
