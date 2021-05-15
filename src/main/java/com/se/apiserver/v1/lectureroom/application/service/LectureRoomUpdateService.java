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
  public Long update(LectureRoomUpdateDto.Request request) {
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
      lectureRoom.updateBuilding(request.getBuilding());

    if(request.getRoomNumber() != null)
      lectureRoom.updateRoomNumber(request.getRoomNumber());

    if(request.getCapacity() != null)
      lectureRoom.updateCapacity(request.getCapacity());

    if(request.getNote() != null){
      String note = request.getNote().isEmpty() ? null : request.getNote();
      lectureRoom.updateNote(note);
    }
    
    // 저장
    return lectureRoomJpaRepository.save(lectureRoom).getLectureRoomId();
  }
}
