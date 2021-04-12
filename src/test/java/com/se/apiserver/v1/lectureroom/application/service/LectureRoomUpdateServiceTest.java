package com.se.apiserver.v1.lectureroom.application.service;

import com.se.apiserver.v1.common.domain.exception.BusinessException;
import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.application.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.application.dto.LectureRoomUpdateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomUpdateServiceTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomUpdateService lectureRoomUpdateService;

  @Test
  void 강의실_수정_정원_수정_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    Long id = lectureRoom.getLectureRoomId();

    // When
    LectureRoomUpdateDto.Request request = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(330)
        .capacity(50)
        .build();

    lectureRoomUpdateService.update(request);

    LectureRoom response = lectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));

    // Then
    Assertions.assertThat(response.getBuilding()).isEqualTo("D");
    Assertions.assertThat(response.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(response.getCapacity()).isEqualTo(50);
  }

  @Test
  void 강의실_수정_건물_수정_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    Long id = lectureRoom.getLectureRoomId();

    // When
    LectureRoomUpdateDto.Request request = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("DB")
        .roomNumber(330)
        .capacity(30)
        .build();

    lectureRoomUpdateService.update(request);

    LectureRoom response = lectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));
    // Then
    Assertions.assertThat(response.getBuilding()).isEqualTo("DB");
    Assertions.assertThat(response.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(response.getCapacity()).isEqualTo(30);
  }

  @Test
  void 강의실_수정_호수_수정_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    Long id = lectureRoom.getLectureRoomId();

    // When
    LectureRoomUpdateDto.Request request = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(331)
        .capacity(30)
        .build();

    lectureRoomUpdateService.update(request);

    LectureRoom response = lectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));
    // Then
    Assertions.assertThat(response.getBuilding()).isEqualTo("D");
    Assertions.assertThat(response.getRoomNumber()).isEqualTo(331);
    Assertions.assertThat(response.getCapacity()).isEqualTo(30);
  }

  @Test
  void 강의실_수정_변경사항_없는_성공(){
    // Given
    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    Long id = lectureRoom.getLectureRoomId();

    // When
    LectureRoomUpdateDto.Request request = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();


    lectureRoomUpdateService.update(request);

    LectureRoom response = lectureRoomJpaRepository
        .findById(id)
        .orElseThrow(() -> new BusinessException(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM));

    // Then
    Assertions.assertThat(response.getBuilding()).isEqualTo("D");
    Assertions.assertThat(response.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(response.getCapacity()).isEqualTo(30);
  }

  @Test
  void 강의실_수정_존재하지_않는_강의실_실패(){
    // Given
    Long id = 777L;

    // When
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(331)
        .capacity(30)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureRoomUpdateService.update(req);
    }).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

  @Test
  void 강의실_수정_강의실_중복_실패(){
    // Given
    lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("DB")
        .roomNumber(107)
        .capacity(30)
        .build());

    LectureRoom lectureRoom = lectureRoomJpaRepository.save(LectureRoom.builder()
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build());

    Long id = lectureRoom.getLectureRoomId();

    // When
    LectureRoomUpdateDto.Request request = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("DB")
        .roomNumber(107)
        .capacity(50)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureRoomUpdateService.update(request);
    }).hasMessage(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM.getMessage());
  }
}
