package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomCreateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureRoomCreateService {

  private final LectureRoomJpaRepository lectureRoomJpaRepository;
  private final LectureRoomQueryRepository lectureRoomQueryRepository;

  @Transactional
  public Long create(LectureRoomCreateDto.Request request){
    // 건물명과 호수가 겹치는 강의실이 있는지 검사
    if(lectureRoomQueryRepository.findByRoomNumberWithBuilding(request.getBuilding(), request.getRoomNumber()).isPresent()){
      throw new BusinessException(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM);
    }

    LectureRoom lectureRoom = new LectureRoom(
        request.getBuilding(),
        request.getRoomNumber(),
        request.getCapacity(),
        request.getNote());

    lectureRoomJpaRepository.save(lectureRoom);

    return lectureRoom.getLectureRoomId();
  }
}
