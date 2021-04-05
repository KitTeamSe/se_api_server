package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomUpdateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureRoomUpdateService {

  private final LectureRoomJpaRepository lectureRoomJpaRepository;
  private final LectureRoomQueryRepository lectureRoomQueryRepository;

  @Transactional
  public LectureRoomReadDto.Response update(LectureRoomUpdateDto.Request request) {
    // DB에서 pk로 탐색
    LectureRoom lectureRoom = lectureRoomJpaRepository
        .findById(request.getLectureRoomId())
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));

    // 변경사항에 건물명이 있다면 추가
    String changeBuilding = lectureRoom.getBuilding();
    if(request.getBuilding() != null)
      changeBuilding = request.getBuilding();

    // 변경사항에 호수가 있다면 추가
    Integer changeRoomNumber = lectureRoom.getRoomNumber();
    if(request.getRoomNumber() != null)
      changeRoomNumber = request.getRoomNumber();

    // DB에서 변경사항대로 중복되는 강의실 있는지 탐색
    Optional<LectureRoom> duplicateCheckLectureRoom = lectureRoomQueryRepository.
        findByRoomNumberWithBuilding(changeBuilding, changeRoomNumber);

    // 중복되는 강의실이 있는데, pk가 다르다면 중복 강의실 존재. 예외 throw
    if(duplicateCheckLectureRoom.isPresent()){
      if(!duplicateCheckLectureRoom.get().getLectureRoomId().equals(lectureRoom.getLectureRoomId()))
        throw new BusinessException(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM);
    }

    if(request.getBuilding() != null)
      updateBuilding(lectureRoom, request.getBuilding());

    if(request.getRoomNumber() != null)
      updateRoomNumber(lectureRoom, request.getRoomNumber());

    if(request.getCapacity() != null)
      updateCapacity(lectureRoom, request.getCapacity());
    
    // 저장
    lectureRoomJpaRepository.save(lectureRoom);
    return LectureRoomReadDto.Response.fromEntity(lectureRoom);
  }

  public void updateBuilding(LectureRoom lectureRoom, String building){
    lectureRoom.updateBuilding(building);
  }

  public void updateRoomNumber(LectureRoom lectureRoom, Integer roomNumber){
    lectureRoom.updateRoomNumber(roomNumber);
  }

  public void updateCapacity(LectureRoom lectureRoom, Integer capacity){
    lectureRoom.updateCapacity(capacity);
  }
}
