package com.se.apiserver.v1.usablelectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.usablelectureroom.application.error.UsableLectureRoomErrorCode;
import com.se.apiserver.v1.usablelectureroom.domain.entity.UsableLectureRoom;
import com.se.apiserver.v1.usablelectureroom.infra.repository.UsableLectureRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsableLectureRoomDeleteService {

  private final UsableLectureRoomJpaRepository usableLectureRoomJpaRepository;

  @Transactional
  public void delete(Long usableLectureRoomId){
    UsableLectureRoom usableLectureRoom = usableLectureRoomJpaRepository
        .findById(usableLectureRoomId)
        .orElseThrow(() ->
            new BusinessException(UsableLectureRoomErrorCode.NO_SUCH_USABLE_LECTURE_ROOM)
        );
    usableLectureRoomJpaRepository.delete(usableLectureRoom);
  }

}
