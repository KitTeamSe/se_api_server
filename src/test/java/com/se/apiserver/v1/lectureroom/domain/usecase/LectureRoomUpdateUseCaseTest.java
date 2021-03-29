package com.se.apiserver.v1.lectureroom.domain.usecase;

import com.se.apiserver.v1.lectureroom.domain.entity.LectureRoom;
import com.se.apiserver.v1.lectureroom.domain.error.LectureRoomErrorCode;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomReadDto;
import com.se.apiserver.v1.lectureroom.infra.dto.LectureRoomUpdateDto;
import com.se.apiserver.v1.lectureroom.infra.repository.LectureRoomJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LectureRoomUpdateUseCaseTest {

  @Autowired
  LectureRoomJpaRepository lectureRoomJpaRepository;

  @Autowired
  LectureRoomUpdateUseCase lectureRoomUpdateUseCase;

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
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(330)
        .capacity(50)
        .build();

    LectureRoomReadDto.Response res = lectureRoomUpdateUseCase.update(req);

    // Then
    Assertions.assertThat(res.getBuilding()).isEqualTo("D");
    Assertions.assertThat(res.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(res.getCapacity()).isEqualTo(50);
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
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("DB")
        .roomNumber(330)
        .capacity(30)
        .build();

    LectureRoomReadDto.Response res = lectureRoomUpdateUseCase.update(req);

    // Then
    Assertions.assertThat(res.getBuilding()).isEqualTo("DB");
    Assertions.assertThat(res.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(res.getCapacity()).isEqualTo(30);
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
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(331)
        .capacity(30)
        .build();

    LectureRoomReadDto.Response res = lectureRoomUpdateUseCase.update(req);

    // Then
    Assertions.assertThat(res.getBuilding()).isEqualTo("D");
    Assertions.assertThat(res.getRoomNumber()).isEqualTo(331);
    Assertions.assertThat(res.getCapacity()).isEqualTo(30);
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
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("D")
        .roomNumber(330)
        .capacity(30)
        .build();

    LectureRoomReadDto.Response res = lectureRoomUpdateUseCase.update(req);

    // Then
    Assertions.assertThat(res.getBuilding()).isEqualTo("D");
    Assertions.assertThat(res.getRoomNumber()).isEqualTo(330);
    Assertions.assertThat(res.getCapacity()).isEqualTo(30);
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
      lectureRoomUpdateUseCase.update(req);
    }).hasMessage(LectureRoomErrorCode.NO_SUCH_LECTURE_ROOM.getMessage());
  }

  @Test
  void 강의실_수정_강의실_중복_실패(){
    // Given
    LectureRoom DB107 = lectureRoomJpaRepository.save(LectureRoom.builder()
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
    LectureRoomUpdateDto.Request req = LectureRoomUpdateDto.Request.builder()
        .lectureRoomId(id)
        .building("DB")
        .roomNumber(107)
        .capacity(50)
        .build();

    // Then
    Assertions.assertThatThrownBy(() -> {
      lectureRoomUpdateUseCase.update(req);
    }).hasMessage(LectureRoomErrorCode.DUPLICATED_LECTURE_ROOM.getMessage());
  }
}
