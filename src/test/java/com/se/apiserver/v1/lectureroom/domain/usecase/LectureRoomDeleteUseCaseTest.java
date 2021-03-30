package com.se.apiserver.v1.lectureroom.domain.usecase;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomDeleteDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomQueryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomDeleteUseCaseTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomDeleteUseCase lectureRoomDeleteUseCase;

  @Test
  void 강의실_삭제_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    // When
    Long id = lectureRoom.getLectureRoomId();
    lectureRoomDeleteUseCase.delete(id);

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
      lectureRoomDeleteUseCase.delete(id);
    }).isInstanceOf(BusinessException.class).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

}
