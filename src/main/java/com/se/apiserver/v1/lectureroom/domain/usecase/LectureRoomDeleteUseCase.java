package com.se.apiserver.v1.lectureroom.domain.usecase;

import com.se.apiserver.v1.common.domain.usecase.UseCase;
import com.se.apiserver.v1.common.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomDeleteDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureRoomDeleteUseCase {

  private final LectureRoomJpaRepository lectureRoomJpaRepository;

  @Transactional
  public void delete(Long lectureRoomId){
    LectureRoom lectureRoom = lectureRoomJpaRepository.findById(lectureRoomId).orElseThrow(()->
        new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM)
    );

    lectureRoomJpaRepository.delete(lectureRoom);
  }
}
