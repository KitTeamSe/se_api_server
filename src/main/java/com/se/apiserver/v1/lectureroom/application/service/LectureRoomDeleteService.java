package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureRoomDeleteService {

  private final LectureRoomJpaRepository lectureRoomJpaRepository;

  @Transactional
  public void delete(Long lectureRoomId){
    LectureRoom lectureRoom = lectureRoomJpaRepository
        .findById(lectureRoomId)
        .orElseThrow(()-> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM)
    );

    lectureRoomJpaRepository.delete(lectureRoom);
  }

}
