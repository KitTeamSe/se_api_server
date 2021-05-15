package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomDeleteServiceTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomDeleteService lectureRoomDeleteService;

  @Test
  void 강의실_삭제_성공(){
    // Given
    LectureRoom lectureRoom = LectureRoomCreateServiceTest
        .createLectureRoom(lectureRoomJpaRepository, "D", 330);

    Long id = lectureRoom.getLectureRoomId();

    // When
    lectureRoomDeleteService.delete(id);

    // Then
    Assertions.assertThat(lectureRoomJpaRepository.findById(id).isEmpty()).isEqualTo(true);
  }

  @Test
  void 강의실_미존재_삭제_실패(){
    // Given
    Long id = 999L;

    // When
    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureRoomDeleteService.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

}
